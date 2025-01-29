package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.AuthorizationDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.TransactionStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers.TransactionMapper;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.http_client.HttpClient;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.http_client.UtilDeviToolsClient;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.notifications.EmailNotification;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Transaction;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.TransferStatus;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.email.EmailNotificationFailedException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer.*;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.TransactionRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_ONE_TENTH_SECOND = 100;

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final HttpClient utilDeviToolsClient;

    @Autowired
    public TransferService(UserRepository userRepository, TransactionRepository transactionRepository, UtilDeviToolsClient utilDeviToolsClient, EmailNotification emailNotification) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.utilDeviToolsClient = utilDeviToolsClient;
    }

    @Transactional
    public Transaction transfer(@Valid TransactionStoreDTO transactionStoreDTO) {
        User payer = this.userRepository.findById(transactionStoreDTO.getPayer()).orElse(null);
        this.throwExceptionIfUserNotExists(payer, transactionStoreDTO.getPayer());
        this.throwExceptionIfPayerIsNotCommon(payer, transactionStoreDTO.getPayer());

        User payee = this.userRepository.findById(transactionStoreDTO.getPayee()).orElse(null);
        this.throwExceptionIfUserNotExists(payee, transactionStoreDTO.getPayee());
        this.throwExceptionIfPayerIsPayee(payer, payee);

        this.throwExceptionIfPayerHasPendingTransfers(payer);

        BigDecimal balancePayer = payer.getWallet().getBalance();
        BigDecimal valueOfTransfer = transactionStoreDTO.getValue();
        this.throwExceptionIfTransferValueIsInvalid(valueOfTransfer);
        this.throwExceptionIfInsufficientBalancePayer(balancePayer, valueOfTransfer);
        this.verifyTransferAuthorization();

        Transaction transaction = TransactionMapper.toEntityStore(transactionStoreDTO, payer, payee, TransferStatus.pending);
        transaction = this.transactionRepository.saveAndFlush(transaction);

        BigDecimal newPayeeWalletBalance = payee.getWallet().getBalance().add(transactionStoreDTO.getValue());
        payee.getWallet().setBalance(newPayeeWalletBalance);

        BigDecimal newPayerWalletBalance = payer.getWallet().getBalance().subtract(transactionStoreDTO.getValue());
        payer.getWallet().setBalance(newPayerWalletBalance);

        payee = this.userRepository.saveAndFlush(payee);
        payer = this.userRepository.saveAndFlush(payer);

        transaction.setStatus(TransferStatus.finalized);
        this.transactionRepository.save(transaction);
        this.sendNotificationsToPayerAndPayee(transactionStoreDTO, payee, payer);

        return transaction;
    }

    private void sendNotificationsToPayerAndPayee(TransactionStoreDTO transactionStoreDTO, User payee, User payer)  {

        boolean isSend = false;
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            try {
                this.utilDeviToolsClient.post("/v1/notify", null, null);
                isSend = true;
                break;
            }  catch (Exception ignored) {
            } finally {
                attempt++;
                this.addDelay(attempt, BACKOFF_ONE_TENTH_SECOND);
            }
        }
        if (!isSend){
            throw new EmailNotificationFailedException(
                    "It was not possible to send the email notifications to the payer and payee.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addDelay(int attempt, long initialBackoffWith1s) {
        if (attempt < MAX_ATTEMPTS) {
            try {
                Thread.sleep(initialBackoffWith1s);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void throwExceptionIfUserNotExists(User user, Long id) {
        if (user == null) {
            throw new UserNotFoundException(
                    String.format("User with ID %d was not found.", id),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    private void throwExceptionIfPayerIsNotCommon(User user, Long id) {
        if (user != null) {
            String categoryNameUser = user.getCategory().getName().name();
            String commonUserCategory = CategoryUserNameEnum.common.name();
            boolean isCommonUser = categoryNameUser.equals(commonUserCategory);
            if (!isCommonUser) {
                throw new UserCannotMakeTransfers(
                        String.format("User with ID %d cannot make transfers.", id),
                        HttpStatus.BAD_REQUEST
                );
            }
        }
    }

    private void throwExceptionIfPayerIsPayee(User payer, User payee) {
        if (payer.getId().equals(payee.getId())) {
            throw new CannotTransferMoneyToThemselvesException(
                    "A user cannot transfer money to themselves.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void throwExceptionIfInsufficientBalancePayer(BigDecimal balancePayer, BigDecimal valueOfTransfer) {
        if (balancePayer.compareTo(valueOfTransfer) < 0) {
            throw new InsufficientBalanceException(
                    "The payer's balance is insufficient to complete the transfer.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void throwExceptionIfTransferValueIsInvalid(BigDecimal valueOfTransfer) {
        if (valueOfTransfer.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferValueMustBeGreaterThanZeroException(
                    "Transfer value must be greater than zero.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void throwExceptionIfPayerHasPendingTransfers(User payer) {
        List<Transaction> payerHasPendingTransfers = this.transactionRepository.findPendingTransfersWithPayerUser(payer.getId());
        if (!payerHasPendingTransfers.isEmpty()){
            throw new PayerHasPendingTransfers(
                    "The payer has pending transfers and cannot initiate a new one until they are resolved.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void verifyTransferAuthorization() {
        AuthorizationDTO authorizationDTO = null;
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            try {
                authorizationDTO = this.utilDeviToolsClient.get("/v2/authorize", AuthorizationDTO.class);
                if (authorizationDTO != null && authorizationDTO.getData().isAuthorized()) {
                    break;
                }
            } catch (RuntimeException ignored) {
            } finally {
                attempt++;
                addDelay(attempt, BACKOFF_ONE_TENTH_SECOND);
            }
        }
        if (authorizationDTO == null || !authorizationDTO.getData().isAuthorized()){
            throw new UnauthorizedTransferException(
                    "Authorization failed: Unauthorized transfer",
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}
