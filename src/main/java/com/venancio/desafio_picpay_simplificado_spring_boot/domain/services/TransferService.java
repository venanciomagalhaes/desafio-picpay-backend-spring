package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.TransactionStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers.TransactionMapper;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Transaction;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.TransferStatus;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer.*;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.TransactionRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de transferência de valores entre usuários.
 * Realiza as validações necessárias e envia notificações sobre o status da transação.
 */
@Service
public class TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;

    @Autowired
    public TransferService(UserRepository userRepository, TransactionRepository transactionRepository,
                           AuthorizationService authorizationService, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
    }

    /**
     * Realiza a transferência entre dois usuários.
     *
     * @param transactionStoreDTO DTO contendo os dados da transação.
     * @return A transação realizada.
     * @throws UserNotFoundException Se o usuário pagador ou recebedor não forem encontrados.
     * @throws UserCannotMakeTransfers Se o pagador não puder realizar transferências.
     * @throws CannotTransferMoneyToThemselvesException Se o pagador tentar transferir para si mesmo.
     * @throws InsufficientBalanceException Se o pagador não tiver saldo suficiente.
     * @throws TransferValueMustBeGreaterThanZeroException Se o valor da transferência for inválido.
     * @throws PayerHasPendingTransfers Se o pagador tiver transferências pendentes.
     * @throws UnauthorizedTransferException Se a transferência não for autorizada.
     */
    @Transactional
    public Transaction transfer(@Valid TransactionStoreDTO transactionStoreDTO) {
        logger.info("Iniciando transferência de {} para o usuário {}", transactionStoreDTO.getValue(), transactionStoreDTO.getPayee());

        User payer = this.userRepository.findById(UUID.fromString(transactionStoreDTO.getPayer())).orElse(null);
        this.throwExceptionIfUserNotExists(payer, UUID.fromString(transactionStoreDTO.getPayer()));
        this.throwExceptionIfPayerIsNotCommon(payer, UUID.fromString(transactionStoreDTO.getPayer()));

        User payee = this.userRepository.findById(UUID.fromString(transactionStoreDTO.getPayee())).orElse(null);
        this.throwExceptionIfUserNotExists(payee, UUID.fromString(transactionStoreDTO.getPayee()));
        this.throwExceptionIfPayerIsPayee(payer, payee);

        this.throwExceptionIfPayerHasPendingTransfers(payer);

        BigDecimal balancePayer = payer.getWallet().getBalance();
        BigDecimal valueOfTransfer = transactionStoreDTO.getValue();
        this.throwExceptionIfTransferValueIsInvalid(valueOfTransfer);
        this.throwExceptionIfInsufficientBalancePayer(balancePayer, valueOfTransfer);

        logger.info("Verificando autorização da transferência...");
        this.authorizationService.verifyTransferAuthorization();

        Transaction transaction = TransactionMapper.toEntityStore(transactionStoreDTO, payer, payee, TransferStatus.pending);
        transaction = this.transactionRepository.saveAndFlush(transaction);

        logger.info("Transação iniciada (pendente) com ID: {}", transaction.getId());

        BigDecimal newPayeeWalletBalance = payee.getWallet().getBalance().add(transactionStoreDTO.getValue());
        payee.getWallet().setBalance(newPayeeWalletBalance);

        BigDecimal newPayerWalletBalance = payer.getWallet().getBalance().subtract(transactionStoreDTO.getValue());
        payer.getWallet().setBalance(newPayerWalletBalance);

        this.userRepository.save(payee);
        this.userRepository.save(payer);

        transaction.setStatus(TransferStatus.finalized);
        this.transactionRepository.save(transaction);

        this.notificationService.sendNotificationsToPayerAndPayee(transactionStoreDTO, payee, payer);

        logger.info("Transferência de {} para o usuário {} concluída com sucesso!", transactionStoreDTO.getValue(), transactionStoreDTO.getPayee());
        return transaction;
    }

    /**
     * Lança uma exceção caso o usuário não seja encontrado.
     *
     * @param user O usuário a ser verificado.
     * @param id O ID do usuário.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    private void throwExceptionIfUserNotExists(User user, UUID id) {
        if (user == null) {
            logger.error("Usuário com ID {} não encontrado", id);
            UserNotFoundException.throwDefaultMessage(id);
        }
    }

    /**
     * Lança uma exceção caso o pagador não possa realizar transferências.
     *
     * @param user O usuário a ser verificado.
     * @param id O ID do usuário.
     * @throws UserCannotMakeTransfers Se o pagador não puder realizar transferências.
     */
    private void throwExceptionIfPayerIsNotCommon(User user, UUID id) {
        if (user != null) {
            String categoryNameUser = user.getCategory().getName().name();
            String commonUserCategory = CategoryUserNameEnum.common.name();
            boolean isCommonUser = categoryNameUser.equals(commonUserCategory);
            if (!isCommonUser) {
                logger.error("Usuário com ID {} não pode realizar transferências", id);
                UserCannotMakeTransfers.throwDefaultMessage(id);
            }
        }
    }

    /**
     * Lança uma exceção caso o pagador tente transferir para si mesmo.
     *
     * @param payer O usuário pagador.
     * @param payee O usuário recebedor.
     * @throws CannotTransferMoneyToThemselvesException Se o pagador tentar transferir para si mesmo.
     */
    private void throwExceptionIfPayerIsPayee(User payer, User payee) {
        if (payer.getId().equals(payee.getId())) {
            logger.error("O pagador e o recebedor não podem ser o mesmo usuário");
            CannotTransferMoneyToThemselvesException.throwDefaultMessage();
        }
    }

    /**
     * Lança uma exceção caso o saldo do pagador seja insuficiente.
     *
     * @param balancePayer O saldo do pagador.
     * @param valueOfTransfer O valor da transferência.
     * @throws InsufficientBalanceException Se o saldo for insuficiente.
     */
    private void throwExceptionIfInsufficientBalancePayer(BigDecimal balancePayer, BigDecimal valueOfTransfer) {
        if (balancePayer.compareTo(valueOfTransfer) < 0) {
            logger.error("Saldo insuficiente para o pagador realizar a transferência de {}", valueOfTransfer);
            InsufficientBalanceException.throwDefaultMessage();
        }
    }

    /**
     * Lança uma exceção caso o valor da transferência seja inválido (menor ou igual a zero).
     *
     * @param valueOfTransfer O valor da transferência.
     * @throws TransferValueMustBeGreaterThanZeroException Se o valor for inválido.
     */
    private void throwExceptionIfTransferValueIsInvalid(BigDecimal valueOfTransfer) {
        if (valueOfTransfer.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("O valor da transferência deve ser maior que zero");
            TransferValueMustBeGreaterThanZeroException.throwDefaultMessage();
        }
    }

    /**
     * Lança uma exceção caso o pagador tenha transferências pendentes.
     *
     * @param payer O pagador.
     * @throws PayerHasPendingTransfers Se o pagador tiver transferências pendentes.
     */
    private void throwExceptionIfPayerHasPendingTransfers(User payer) {
        List<Transaction> payerHasPendingTransfers = this.transactionRepository.findPendingTransfersWithPayerUser(payer.getId());
        if (!payerHasPendingTransfers.isEmpty()) {
            logger.error("O pagador com ID {} possui transferências pendentes", payer.getId());
            PayerHasPendingTransfers.throwDefaultMessage();
        }
    }
}
