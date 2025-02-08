package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.Transfer;

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
    private final TransferValidator transferValidator;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;

    @Autowired
    public TransferService(TransferValidator transferValidator, UserRepository userRepository, TransactionRepository transactionRepository,
                           AuthorizationService authorizationService, NotificationService notificationService) {
        this.transferValidator = transferValidator;
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
        this.transferValidator.throwExceptionIfUserNotExists(payer, UUID.fromString(transactionStoreDTO.getPayer()));
        this.transferValidator.throwExceptionIfPayerIsNotCommon(payer, UUID.fromString(transactionStoreDTO.getPayer()));

        User payee = this.userRepository.findById(UUID.fromString(transactionStoreDTO.getPayee())).orElse(null);
        this.transferValidator.throwExceptionIfUserNotExists(payee, UUID.fromString(transactionStoreDTO.getPayee()));
        this.transferValidator.throwExceptionIfPayerIsPayee(payer, payee);

        List<Transaction> payerHasPendingTransfers = this.transactionRepository.findPendingTransfersWithPayerUser(payer.getId());
        this.transferValidator.throwExceptionIfPayerHasPendingTransfers(payerHasPendingTransfers, payer);

        BigDecimal balancePayer = payer.getWallet().getBalance();
        BigDecimal valueOfTransfer = transactionStoreDTO.getValue();
        this.transferValidator.throwExceptionIfTransferValueIsInvalid(valueOfTransfer);
        this.transferValidator.throwExceptionIfInsufficientBalancePayer(balancePayer, valueOfTransfer);

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

}
