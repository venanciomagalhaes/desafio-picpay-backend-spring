package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user.CategoryUserAlreadyExistsException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user.CategoryUserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CategoryUserServiceIT {

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    @Autowired
    private CategoryUserService categoryUserService;

    private CategoryUser existingCategory;

    @BeforeEach
    void setUp() {
        categoryUserRepository.deleteAll();
        existingCategory = new CategoryUser();
        existingCategory.setName(CategoryUserNameEnum.common);
        categoryUserRepository.saveAndFlush(existingCategory);
    }

    @Test
    @DisplayName("Deve criar e persistir uma categoria de usuário com sucesso")
    void storeCategoryUser() {
        this.categoryUserRepository.deleteAll();
        CategoryUserStoreDTO dto = new CategoryUserStoreDTO("common");
        CategoryUser createdCategoryUser = categoryUserService.store(dto);
        CategoryUser retrievedCategoryUser = categoryUserRepository.findById(createdCategoryUser.getId()).orElseThrow();
        assertEquals(CategoryUserNameEnum.common, retrievedCategoryUser.getName());
    }

    @Test
    @DisplayName("Não deve criar uma categoria de usuário com nome duplicado")
    void storeCategoryUserAlreadyExists() {
        CategoryUserStoreDTO dto = new CategoryUserStoreDTO("common");
        assertThrows(CategoryUserAlreadyExistsException.class, () -> {
            categoryUserService.store(dto);
        });
    }

    @Test
    @DisplayName("Deve recuperar uma categoria de usuário por ID com sucesso")
    void showCategoryUser() {
        CategoryUser retrievedCategoryUser = categoryUserService.show(existingCategory.getId());

        assertEquals(CategoryUserNameEnum.common, retrievedCategoryUser.getName());
    }

    @Test
    @DisplayName("Não deve recuperar uma categoria de usuário com ID inexistente")
    void showCategoryUserNotFound() {
        assertThrows(CategoryUserNotFoundException.class, () -> {
            categoryUserService.show(UUID.randomUUID());
        });
    }

    @Test
    @DisplayName("Deve atualizar uma categoria de usuário com sucesso")
    void updateCategoryUser() {
        CategoryUserUpdateDTO dto = new CategoryUserUpdateDTO("store");
        CategoryUser updatedCategoryUser = categoryUserService.update(existingCategory.getId(), dto);
        assertEquals(CategoryUserNameEnum.store, updatedCategoryUser.getName());
    }

    @Test
    @DisplayName("Não deve atualizar uma categoria de usuário com ID inexistente")
    void updateCategoryUserNotFound() {

        this.categoryUserRepository.deleteById(existingCategory.getId());

        CategoryUserUpdateDTO dto = new CategoryUserUpdateDTO("common");
        assertThrows(CategoryUserNotFoundException.class, () -> {
            categoryUserService.update(UUID.randomUUID(), dto);
        });
    }

    @Test
    @DisplayName("Deve excluir uma categoria de usuário com sucesso")
    void deleteCategoryUser() {
        categoryUserService.delete(existingCategory.getId());

        assertThrows(CategoryUserNotFoundException.class, () -> {
            categoryUserService.show(existingCategory.getId());
        });
    }

    @Test
    @DisplayName("Não deve excluir uma categoria de usuário com ID inexistente")
    void deleteCategoryUserNotFound() {
        assertThrows(CategoryUserNotFoundException.class, () -> {
            categoryUserService.delete(UUID.randomUUID()); // ID inexistente
        });
    }
}
