package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.AuthorizationDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.TransactionStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.http_client.UtilDeviToolsClient;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.notifications.EmailNotification;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Transaction;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.TransferStatus;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.email.EmailNotificationFailedException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer.*;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.TransactionRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.UserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.WalletRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.Transfer.AuthorizationService;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.Transfer.NotificationService;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.Transfer.TransferService;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.Transfer.TransferValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

    @Autowired
    private TransferValidator transferValidator;

    private User payer;
    private User payee;

    @Autowired
    private UserService userService;


    @BeforeEach
    void setUp() {
        // Criação de uma categoria de usuário
        CategoryUser common = new CategoryUser(CategoryUserNameEnum.common);
        common = categoryUserRepository.save(common);

        // Criação dos usuários

        UserStoreDTO storeDTO = new UserStoreDTO(
            "Payer",
            "12345678901",
            "payer@domain.com",
            "password",
            common.getId().toString()
        );

        this.payer  = this.userService.store(storeDTO);

        storeDTO = new UserStoreDTO(
                "Payee",
                "98765432100",
                "payee@domain.com",
                "password",
                common.getId().toString()
        );


        this.payee = this.userService.store(storeDTO);

    }

    @Test
    @DisplayName("Transferência realizada com sucesso")
    void transferSuccessfulTest() {
        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(100),
                this.payer.getId().toString(),
                this.payee.getId().toString()
        );

        when(utilDeviToolsClientMock.get("/v2/authorize", AuthorizationDTO.class)).thenReturn(
              new AuthorizationDTO(
                      "success",
                      new AuthorizationDTO.Data(
                              true
                      )
              )
        );

        when(utilDeviToolsClientMock.post("/v1/notify", null, null)).thenReturn(
                true
        );


        AuthorizationService authorizationService = new AuthorizationService(
                utilDeviToolsClientMock
        );

        NotificationService notificationService = new NotificationService(
                utilDeviToolsClientMock
        );

        TransferService transferService = new TransferService(
                transferValidator,
                userRepository,
                transactionRepository,
                authorizationService,
                notificationService
        );

        Transaction transaction = transferService.transfer(transactionDTO);



        assertNotNull(transaction);
        assertEquals(TransferStatus.finalized, transaction.getStatus());
        assertEquals(transaction.getValue(), BigDecimal.valueOf(100));
        assertEquals( BigDecimal.valueOf(900), payer.getWallet().getBalance());
        assertEquals(BigDecimal.valueOf(1100), payee.getWallet().getBalance());
    }

    @Test
    @DisplayName("Erro ao realizar transferência - Usuário não encontrado")
    void userNotFoundTest() {
        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(100),
                UUID.randomUUID().toString(),
                payee.getId().toString()
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
                payer.getId().toString(),
                payee.getId().toString()
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
                payer.getId().toString(),
                payer.getId().toString()
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
                payer.getId().toString(),
                payee.getId().toString()
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
                payer.getId().toString(),
                payee.getId().toString()
        );

        assertThrows(PayerHasPendingTransfers.class, () -> {
            transferService.transfer(transactionDTO);
        });
    }

    @Test
    @DisplayName("Erro ao realizar transferência - Transferência não autorizada")
    void unauthorizedTransferTest() {
        when(utilDeviToolsClientMock.get("/v2/authorize", AuthorizationDTO.class)).thenThrow(new RuntimeException());

        AuthorizationService authorizationService = new AuthorizationService(
                utilDeviToolsClientMock
        );

        NotificationService notificationService = new NotificationService(
                utilDeviToolsClientMock
        );

        TransferService transferService = new TransferService(
                transferValidator,
                userRepository,
                transactionRepository,
                authorizationService,
                notificationService
        );

        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(100),
                payer.getId().toString(),
                payee.getId().toString()
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

        AuthorizationService authorizationService = new AuthorizationService(
                utilDeviToolsClientMock
        );

        NotificationService notificationService = new NotificationService(
                utilDeviToolsClientMock
        );

        TransferService transferService = new TransferService(
                transferValidator,
                userRepository,
                transactionRepository,
                authorizationService,
                notificationService
        );

        TransactionStoreDTO transactionDTO = new TransactionStoreDTO(
                BigDecimal.valueOf(100),
                payer.getId().toString(),
                payee.getId().toString()
        );

        assertThrows(EmailNotificationFailedException.class, () -> {
            transferService.transfer(transactionDTO);
        });
    }
}
