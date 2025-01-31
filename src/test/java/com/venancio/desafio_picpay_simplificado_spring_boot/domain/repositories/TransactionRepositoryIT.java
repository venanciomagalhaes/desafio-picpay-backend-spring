package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Transaction;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.TransferStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TransactionRepositoryIT {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    private User payer;

    @BeforeEach
    void setUp() {

        CategoryUser categoryStore = new CategoryUser(
                CategoryUserNameEnum.store
        );

        CategoryUser categoryCommon = new CategoryUser(
                CategoryUserNameEnum.common
        );

        categoryStore = this.categoryUserRepository.saveAndFlush(categoryStore);

        User userMarvel = new User(
                "Marvel",
                "34316951000196",
                "marvel@marvel.com",
                "marvelpassword",
                categoryStore
        );

        User userPeter = new User(
                "Peter",
                "05966126054",
                "peter@marvel.com",
                "marvelpassword",
                categoryCommon
        );

        userMarvel = this.userRepository.saveAndFlush(userMarvel);
        userPeter = this.userRepository.saveAndFlush(userPeter);

        Transaction pendingTransacition = new Transaction(
                null,
                userPeter,
                userMarvel,
                BigDecimal.valueOf(1000),
                TransferStatus.pending,
                null,
                null
        );

        Transaction finalizedTransacition = new Transaction(
                null,
                userPeter,
                userMarvel,
                BigDecimal.valueOf(1000),
                TransferStatus.finalized,
                null,
                null
        );

        this.transactionRepository.saveAllAndFlush(List.of(
                finalizedTransacition,
                pendingTransacition
        ));

        this.payer = userPeter;
    }

    @Test
    @DisplayName("Deve retornar uma transação pendente para o pagador")
    void findPendingTransfersWithPayerUser() {
        List<Transaction> pendingTransactionsList = this.transactionRepository.findPendingTransfersWithPayerUser(this.payer.getId());
        assertEquals(1, pendingTransactionsList.size());
        assertFalse(pendingTransactionsList.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar 0 transações pendentes para o pagador")
    void findPendingTransfersIfNotExistToUser() {
        this.transactionRepository.deleteAll();
        List<Transaction> pendingTransactionsList = this.transactionRepository.findPendingTransfersWithPayerUser(this.payer.getId());
        assertEquals(0, pendingTransactionsList.size());
        assertTrue(pendingTransactionsList.isEmpty());
    }
}
