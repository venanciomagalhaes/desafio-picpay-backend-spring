package com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashSet;

class CategoryUserTest {

    private CategoryUser categoryUser;

    @BeforeEach
    void setUp() {
        categoryUser = new CategoryUser(CategoryUserNameEnum.common);
    }

    @Test
    void testPrePersist() {
        categoryUser.prePersist();
        assertNotNull(categoryUser.getCreatedAt());
        assertNotNull(categoryUser.getUpdatedAt());
        assertEquals(categoryUser.getCreatedAt(), categoryUser.getUpdatedAt());
    }

    @Test
    void testPreUpdate() throws InterruptedException {
        categoryUser.prePersist();
        LocalDateTime firstUpdate = categoryUser.getUpdatedAt();

        Thread.sleep(10);
        categoryUser.preUpdate();

        assertTrue(categoryUser.getUpdatedAt().isAfter(firstUpdate));
    }

    @Test
    void testConstructorWithEnum() {
        CategoryUser newCategoryUser = new CategoryUser(CategoryUserNameEnum.store);
        assertEquals(CategoryUserNameEnum.store, newCategoryUser.getName());
        assertTrue(newCategoryUser.getUsers().isEmpty());
    }

    @Test
    void testSetAndGetMethods() {
        categoryUser.setId(1L);
        categoryUser.setName(CategoryUserNameEnum.common);
        categoryUser.setUsers(new HashSet<>());

        assertEquals(1L, categoryUser.getId());
        assertEquals(CategoryUserNameEnum.common, categoryUser.getName());
        assertNotNull(categoryUser.getUsers());
    }
}
