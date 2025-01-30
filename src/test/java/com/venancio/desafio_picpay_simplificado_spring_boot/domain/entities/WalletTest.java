package com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class WalletTest {

    private Wallet wallet;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        wallet = new Wallet(
                1L,
                BigDecimal.ZERO,
                user,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    void testPrePersist() {
        wallet.prePersist();
        assertNotNull(wallet.getCreatedAt());
        assertNotNull(wallet.getUpdatedAt());
        assertEquals(wallet.getCreatedAt(), wallet.getUpdatedAt());
    }

    @Test
    void testPreUpdate() throws InterruptedException {
        wallet.prePersist();
        LocalDateTime firstUpdate = wallet.getUpdatedAt();

        Thread.sleep(10);
        wallet.preUpdate();

        assertTrue(wallet.getUpdatedAt().isAfter(firstUpdate));
    }

    @Test
    void testSetAndGetMethods() {
        wallet.setId(2L);
        wallet.setBalance(BigDecimal.valueOf(500.0));
        wallet.setUser(new User());

        assertEquals(2L, wallet.getId());
        assertEquals(BigDecimal.valueOf(500.0), wallet.getBalance());
        assertNotNull(wallet.getUser());
    }
}