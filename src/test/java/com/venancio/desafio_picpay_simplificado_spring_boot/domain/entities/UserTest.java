package com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

class UserTest {

    private User user;
    private CategoryUser category;

    @BeforeEach
    void setUp() {
        category = new CategoryUser();
        user = new User(
                "João Silva",
                "12345678900",
                "joao@email.com",
                "senha123",
                category);
    }

    @Test
    void testPrePersist() {
        user.prePersist();
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertEquals(user.getCreatedAt(), user.getUpdatedAt());
    }

    @Test
    void testPreUpdate() throws InterruptedException {
        user.prePersist();
        LocalDateTime firstUpdate = user.getUpdatedAt();

        Thread.sleep(10);
        user.preUpdate();

        assertTrue(user.getUpdatedAt().isAfter(firstUpdate));
    }

    @Test
    void testSetAndGetMethods() {
        UUID random = UUID.randomUUID();
        user.setId(random);
        user.setName("Maria Oliveira");
        user.setCpfCnpj("98765432100");
        user.setEmail("maria@email.com");
        user.setPassword("novaSenha");
        user.setCategory(new CategoryUser());
        user.setPayerTransactions(new HashSet<>());
        user.setPayeeTransactions(new HashSet<>());

        assertEquals(random, user.getId());
        assertEquals("Maria Oliveira", user.getName());
        assertEquals("98765432100", user.getCpfCnpj());
        assertEquals("maria@email.com", user.getEmail());
        assertEquals("novaSenha", user.getPassword());
        assertNotNull(user.getCategory());
        assertNotNull(user.getPayerTransactions());
        assertNotNull(user.getPayeeTransactions());
    }
}