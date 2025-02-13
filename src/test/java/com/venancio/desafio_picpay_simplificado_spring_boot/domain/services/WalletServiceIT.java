package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Wallet;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.UserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class WalletServiceIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;

    private User userSonic;



    @BeforeEach
    void setUp() {
        CategoryUser common = new CategoryUser(CategoryUserNameEnum.common);
        common = this.categoryUserRepository.save(common);

        userSonic = new User(
                "Sonic",
                "12345678901",
                "sonic@sega.com",
                "password",
                common
        );
        userSonic = userRepository.save(userSonic);
    }

    @Test
    @DisplayName("Deve criar e persistir uma carteira com saldo inicial de 1000 para um usuário")
    void createABlankWallet() {
        Wallet createdWallet = walletService.createABlankWallet(userSonic);
        Wallet retrievedWallet = walletRepository.findById(createdWallet.getId()).orElseThrow();

        assertEquals(0, BigDecimal.valueOf(1000.00).compareTo(retrievedWallet.getBalance()));
        assertEquals(userSonic.getId(), retrievedWallet.getUser().getId());
    }
}
