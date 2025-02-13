package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class CategoryUserRepositoryIT {

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    @BeforeEach
    void setUp() {
        CategoryUser common = new CategoryUser(CategoryUserNameEnum.common);
        this.categoryUserRepository.save(common);
    }

    @Test
    @DisplayName("Deve retornar a categoria existente quando procurada por nome")
    void findByName() {
        List<CategoryUser> categoryUsersByName = this.categoryUserRepository.findByName(CategoryUserNameEnum.common);
        assertEquals(1, categoryUsersByName.size());
        assertEquals(CategoryUserNameEnum.common, categoryUsersByName.getFirst().getName());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver categorias com o nome desejado")
    void findByNameIfNonExistsCategories() {
        List<CategoryUser> categoryUsersByName = this.categoryUserRepository.findByName(CategoryUserNameEnum.store);
        assertEquals(0, categoryUsersByName.size());
        assertTrue(categoryUsersByName.isEmpty());
    }
}
