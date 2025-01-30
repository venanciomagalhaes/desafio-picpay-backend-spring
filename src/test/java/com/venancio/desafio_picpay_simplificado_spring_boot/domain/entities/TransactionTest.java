package com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.TransferStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class TransactionTest {

    private Transaction transaction;
    private User payer;
    private User payee;

    @BeforeEach
    void setUp() {
        payer = new User();
        payee = new User();
        transaction = new Transaction(
                1L,
                payer,
                payee,
                BigDecimal.valueOf(100.0),
                TransferStatus.pending,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void testPrePersist() {
        transaction.prePersist();
        assertNotNull(transaction.getCreatedAt());
        assertNotNull(transaction.getUpdatedAt());
        assertEquals(transaction.getCreatedAt(), transaction.getUpdatedAt());
    }

    @Test
    void testPreUpdate() throws InterruptedException {
        transaction.prePersist();
        LocalDateTime firstUpdate = transaction.getUpdatedAt();

        Thread.sleep(10);
        transaction.preUpdate();

        assertTrue(transaction.getUpdatedAt().isAfter(firstUpdate));
    }

    @Test
    void testSetAndGetMethods() {
        transaction.setId(2L);
        transaction.setValue(BigDecimal.valueOf(200.0));
        transaction.setStatus(TransferStatus.finalized);
        assertEquals(2L, transaction.getId());
        assertEquals(transaction.getPayee(), payee);
        assertEquals(transaction.getPayer(), payer);
        assertEquals(BigDecimal.valueOf(200.0), transaction.getValue());
        assertEquals(TransferStatus.finalized, transaction.getStatus());
    }
}