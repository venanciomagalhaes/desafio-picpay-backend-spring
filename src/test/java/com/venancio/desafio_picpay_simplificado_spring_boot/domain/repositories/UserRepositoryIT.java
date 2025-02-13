package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
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
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    private User userMickey;
    private User userDonald;

    @BeforeEach
    void setUp() {

        CategoryUser commonCategory = new CategoryUser(CategoryUserNameEnum.common);
        categoryUserRepository.save(commonCategory);

        userMickey = new User("Mickey Mouse", "12345678901", "mickey@disney.com", "password", commonCategory);
        userDonald = new User("Donald Duck", "98765432100", "donald@disney.com", "password", commonCategory);

        userRepository.saveAll(List.of(userMickey, userDonald));
    }

    @Test
    @DisplayName("Deve encontrar usuário por CPF/CNPJ ou e-mail quando existe")
    void findByCpfCnpjOrEmailExists() {
        List<User> usersByCpf = userRepository.findByCpfCnpjOrEmail("12345678901", "mickey@disney.com");
        assertEquals(1, usersByCpf.size());
    }

    @Test
    @DisplayName("Não deve encontrar usuário por CPF/CNPJ ou e-mail quando não existe")
    void findByCpfCnpjOrEmailNotExists() {
        List<User> usersByCpf = userRepository.findByCpfCnpjOrEmail("00000000000", "unknown@disney.com");
        assertEquals(0, usersByCpf.size());
        assertTrue(usersByCpf.isEmpty());
    }

    @Test
    @DisplayName("Deve verificar se e-mail já pertence a outro usuário quando existe")
    void existsByEmailInAnotherUserExists() {
        List<User> usersWithSameEmail = userRepository.existsByEmailInAnotherUser("mickey@disney.com", userDonald.getId());
        assertEquals(1, usersWithSameEmail.size());
    }

    @Test
    @DisplayName("Não deve encontrar outro usuário com o mesmo e-mail quando não existe")
    void existsByEmailInAnotherUserNotExists() {
        List<User> usersWithSameEmail = userRepository.existsByEmailInAnotherUser("pluto@disney.com", userDonald.getId());
        assertEquals(0, usersWithSameEmail.size());
        assertTrue(usersWithSameEmail.isEmpty());
    }
}