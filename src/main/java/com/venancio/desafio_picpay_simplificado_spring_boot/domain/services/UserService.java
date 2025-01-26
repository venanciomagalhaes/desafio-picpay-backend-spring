package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Wallet;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user.CategoryUserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserAlreadyExistsException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.UserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    @Autowired
    private WalletRepository walletRepository;

    public Page<User> index(Pageable pageable){
        return this.userRepository.findAll(pageable);
    }

    private void verifyUserNotExist(User user, Long id){
        if (user == null){
            throw new BusinessException(
                    "User with the ID " + id + " was not found.",
                    HttpStatus.NOT_FOUND
            );
        }
    }

    public User show(Long id) {
        User user = this.userRepository.findById(id).orElse(null);
        System.out.println(user);
        this.verifyUserNotExist(user, id);
        return user;
    }

    /**
     * Verifica se a categoria de usuário não foi encontrada.
     *
     * @param categoryUser A categoria de usuário a ser verificada.
     * @param id O id da categoria de usuário a ser verificada.
     * @throws CategoryUserNotFoundException Se a categoria de usuário não for encontrada.
     */
    private void verifyCategoryUserNotFound(CategoryUser categoryUser, Long id){
        if (categoryUser == null){
            throw new CategoryUserNotFoundException(
                    "User category with the ID " + id + " was not found.",
                    HttpStatus.NOT_FOUND
            );
        }
    }

    private void verifyUserExist(UserStoreDTO userStoreDTO){
        User userExist = this.userRepository.findByCpfCnpjOrEmail(
                userStoreDTO.cpf_cnpj(),
                userStoreDTO.email()
        ).orElse(null);
        if (userExist != null){
            throw new UserAlreadyExistsException(
                    "There is already a user",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public User store(UserStoreDTO userStoreDTO) {
        this.verifyUserExist(userStoreDTO);
        CategoryUser categoryUser = this.categoryUserRepository.findById(userStoreDTO.category_id()).orElse(null);
        this.verifyCategoryUserNotFound(categoryUser, userStoreDTO.category_id());
        User user = this.userRepository.save(UserStoreDTO.toEntity(userStoreDTO, categoryUser));
        Wallet wallet = this.walletRepository.save(new Wallet(null, BigDecimal.ZERO, user, null, null));
        user.setWallet(wallet);
        return user;
    }
}
