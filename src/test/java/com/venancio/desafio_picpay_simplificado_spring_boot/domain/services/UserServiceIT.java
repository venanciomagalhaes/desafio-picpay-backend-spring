package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    private CategoryUser category;

    @BeforeEach
    void setUp() {
        category = new CategoryUser(CategoryUserNameEnum.common);
        category = categoryUserRepository.save(category);
    }

    @Test
    @DisplayName("Deve criar um usuário corretamente")
    void shouldCreateUserSuccessfully() {
        UserStoreDTO dto = new UserStoreDTO(
                "Novo Usuário",
                "12345678901",
                "novo@email.com",
                "password",
                category.getId());
        User user = userService.store(dto);
        assertNotNull(user.getId());
        assertEquals("Novo Usuário", user.getName());
    }

    @Test
    @DisplayName("Deve recuperar um usuário pelo ID")
    void shouldRetrieveUserById() {
        User user = userRepository.save(new User(
                "Teste",
                "98765432101",
                "teste@email.com",
                "password",
                category)
        );
        User retrievedUser = userService.show(user.getId());
        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getName(), retrievedUser.getName());
        assertEquals(user.getCpfCnpj(), retrievedUser.getCpfCnpj());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
        assertEquals(user.getCategory().getId(), retrievedUser.getCategory().getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário inexistente")
    void shouldThrowExceptionWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.show(999L));
    }

    @Test
    @DisplayName("Deve atualizar um usuário existente")
    void shouldUpdateUserSuccessfully() {
        User user = userRepository.save(
                new User(
                        "Teste",
                        "98765432101",
                        "teste@email.com",
                        "password",
                        category)
        );
        UserUpdateDTO dto = new UserUpdateDTO(
                "Usuário Atualizado",
                "novo@email.com",
                "passwordUpdated",
                category.getId()
        );
        User updatedUser = userService.update(user.getId(), dto);
        assertEquals("Usuário Atualizado", updatedUser.getName());
        assertEquals("novo@email.com", updatedUser.getEmail());
        assertEquals("passwordUpdated", updatedUser.getPassword());
        assertEquals(category.getId(), updatedUser.getCategory().getId());
    }

    @Test
    @DisplayName("Deve excluir um usuário com sucesso")
    void shouldDeleteUserSuccessfully() {
        User user = userRepository.save(
                new User(
                        "Teste",
                        "98765432101",
                        "teste@email.com",
                        "password",
                        category)
        );
        userService.delete(user.getId());
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    @DisplayName("Deve listar usuários paginados")
    void shouldListUsersPaginated() {
        userRepository.save(
                new User(
                        "User1",
                        "11111111111",
                        "user1@email.com",
                        "password",
                        category)
        );
        userRepository.save(
                new User(
                        "User2",
                        "22222222222",
                        "user2@email.com",
                        "password",
                        category)
        );
        Page<User> users = userService.index(PageRequest.of(0, 2));
        assertEquals(2, users.getTotalElements());
    }
}
