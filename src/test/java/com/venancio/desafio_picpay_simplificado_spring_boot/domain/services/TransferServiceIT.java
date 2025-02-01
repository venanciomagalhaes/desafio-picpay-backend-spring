package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.AuthorizationDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.http_client.UtilDeviToolsClient;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.notifications.EmailNotification;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Transaction;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Wallet;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.TransferStatus;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.email.EmailNotificationFailedException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer.*;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.UserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.TransactionRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.TransactionStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class TransferServiceIT {

    @Autowired
    private TransferService transferService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private EmailNotification emailNotification;

    @Mock
    private UtilDeviToolsClient utilDeviToolsClientMock;

    private User payer;
    private User payee;

    @BeforeEach
    void setUp() {
        // Criação de uma categoria de usuário
        CategoryUser common = new CategoryUser(CategoryUserNameEnum.common);
        common = categoryUserRepository.saveAndFlush(common);

        // Criação dos usuários
        this.payer = new User("Payer", "12345678901", "payer@domain.com", "password", common);
        this.payee = new User("Payee", "98765432100", "payee@domain.com", "password", common);

        // Criação das wallets e associação com os usuários
        Wallet payerWallet = new Wallet(   null,
                BigDecimal.valueOf(1000),
                this.payer,
                null,
                null);
        Wallet payeeWallet = new Wallet(   null,
                BigDecimal.valueOf(500),
                this.payee,
                null,
                null);

        // Associa as wallets aos usuários
        this.payer.setWallet(payerWallet);
        this.payee.setWallet(payeeWallet);

        // Salvando os usuários e suas wallets
        this.payer = userRepository.saveAndFlush(this.payer);
        this.payee = userRepository.saveAndFlush(this.payee);
    }

    @Test
    @DisplayName("Transferência realizada com sucesso")
    void transferSuccessfulTest() {
        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(100),
                this.payer.getId(),
                this.payee.getId()
        );

        Transaction transaction = this.transferService.transfer(transactionDTO);

        assertNotNull(transaction);
        assertEquals(TransferStatus.finalized, transaction.getStatus());
        assertEquals(transaction.getValue(), BigDecimal.valueOf(100));
        assertEquals(payer.getWallet().getBalance(), BigDecimal.valueOf(900));
        assertEquals(payee.getWallet().getBalance(), BigDecimal.valueOf(600));
    }

    @Test
    @DisplayName("Erro ao realizar transferência - Usuário não encontrado")
    void userNotFoundTest() {
        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(100),
                999L,
                payee.getId()
        );
        assertThrows(UserNotFoundException.class, () -> {
            transferService.transfer(transactionDTO);
        });
    }

    @Test
    @DisplayName("Erro ao realizar transferência - Saldo insuficiente")
    void insufficientBalanceTest() {
        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(1500),
                payer.getId(),
                payee.getId()
        );
        assertThrows(InsufficientBalanceException.class, () -> {
            transferService.transfer(transactionDTO);
        });
    }

    @Test
    @DisplayName("Erro ao realizar transferência - Transferência para si mesmo")
    void transferToSelfTest() {
        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(100),
                payer.getId(),
                payer.getId()
        );

        assertThrows(CannotTransferMoneyToThemselvesException.class, () -> {
            transferService.transfer(transactionDTO);
        });
    }

    @Test
    @DisplayName("Erro ao realizar transferência - Valor da transferência inválido")
    void invalidTransferValueTest() {
        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(0),
                payer.getId(),
                payee.getId()
        );

        assertThrows(TransferValueMustBeGreaterThanZeroException.class, () -> {
            transferService.transfer(transactionDTO);
        });
    }

    @Test
    @DisplayName("Erro ao realizar transferência - Pagador com transferências pendentes")
    void payerHasPendingTransfersTest() {
        Transaction pendingTransaction = new Transaction(
                null,
                payer,
                payee,
                BigDecimal.valueOf(1000),
                TransferStatus.pending,
                null,
                null
        );

        this.transactionRepository.save(pendingTransaction);

        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(100),
                payer.getId(),
                payee.getId()
        );

        assertThrows(PayerHasPendingTransfers.class, () -> {
            transferService.transfer(transactionDTO);
        });
    }

    @Test
    @DisplayName("Erro ao realizar transferência - Transferência não autorizada")
    void unauthorizedTransferTest() {
        when(utilDeviToolsClientMock.get("/v2/authorize", AuthorizationDTO.class)).thenThrow(new RuntimeException());

        TransferService transferService = new TransferService(
                userRepository,
                transactionRepository,
                utilDeviToolsClientMock,
                emailNotification
        );

        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(100),
                payer.getId(),
                payee.getId()
        );

        assertThrows(UnauthorizedTransferException.class, () -> {
            transferService.transfer(transactionDTO);
        });
    }

    @Test
    @DisplayName("Erro ao realizar transferência - Falha ao enviar notificação por e-mail")
    void setEmailNotificationFailed() {
        when(utilDeviToolsClientMock.get("/v2/authorize", AuthorizationDTO.class)).thenReturn(
                new AuthorizationDTO(
                        "success",
                        new AuthorizationDTO.Data(
                                true
                        )
                )
        );
        when(utilDeviToolsClientMock.post("/v1/notify", null, null)).thenThrow(new RuntimeException());

        TransferService transferService = new TransferService(
                userRepository,
                transactionRepository,
                utilDeviToolsClientMock,
                emailNotification
        );

        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(100),
                payer.getId(),
                payee.getId()
        );

        assertThrows(EmailNotificationFailedException.class, () -> {
            transferService.transfer(transactionDTO);
        });
    }
}
