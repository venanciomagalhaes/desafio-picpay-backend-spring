package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.Transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Transaction;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer.*;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransferValidator {

    private static final Logger logger = LoggerFactory.getLogger(TransferValidator.class);

    /**
     * Lança uma exceção caso o usuário não seja encontrado.
     *
     * @param user O usuário a ser verificado.
     * @param id O ID do usuário.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    public void throwExceptionIfUserNotExists(User user, UUID id) {
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
    public void throwExceptionIfPayerIsNotCommon(User user, UUID id) {
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
    public void throwExceptionIfPayerIsPayee(User payer, User payee) {
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
    public void throwExceptionIfInsufficientBalancePayer(BigDecimal balancePayer, BigDecimal valueOfTransfer) {
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
    public void throwExceptionIfTransferValueIsInvalid(BigDecimal valueOfTransfer) {
        if (valueOfTransfer.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("O valor da transferência deve ser maior que zero");
            TransferValueMustBeGreaterThanZeroException.throwDefaultMessage();
        }
    }

    /**
     * Lança uma exceção caso o pagador tenha transferências pendentes.
     *
     * @param payerHasPendingTransfers Lista de transferências do pagador.
     * @param payer O pagador.
     * @throws PayerHasPendingTransfers Se o pagador tiver transferências pendentes.
     */
    public void throwExceptionIfPayerHasPendingTransfers(List<Transaction> payerHasPendingTransfers, User payer) {
        if (!payerHasPendingTransfers.isEmpty()) {
            logger.error("O pagador com ID {} possui transferências pendentes", payer.getId());
            PayerHasPendingTransfers.throwDefaultMessage();
        }
    }

}
