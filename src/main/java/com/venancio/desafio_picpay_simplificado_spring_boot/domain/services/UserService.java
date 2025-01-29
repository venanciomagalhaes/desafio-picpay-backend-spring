package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers.UserMapper;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Wallet;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user.CategoryUserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.EmailInUseForAnotherUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserAlreadyExistsException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;

    private final CategoryUserRepository categoryUserRepository;

    private final WalletService walletService;

    @Autowired
    public UserService(UserRepository userRepository, CategoryUserRepository categoryUserRepository, WalletService walletService) {
        this.userRepository = userRepository;
        this.categoryUserRepository = categoryUserRepository;
        this.walletService = walletService;
    }

    public Page<User> index(Pageable pageable){
        return this.userRepository.findAll(pageable);
    }

    public User show(Long id) {
        User user = this.userRepository.findById(id).orElse(null);
        this.throwExceptionIfUserNotFound(user, id);
        return user;
    }

    private void throwExceptionIfUserNotFound(User user, Long id){
        if (user == null){
            throw new UserNotFoundException(
                    "User with the ID " + id + " was not found.",
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @Transactional
    public User store(@Valid UserStoreDTO userStoreDTO) {
        this.throwExceptionIfUserAlreadyExist(userStoreDTO);
        CategoryUser categoryUser = this.categoryUserRepository.findById(userStoreDTO.category_id()).orElse(null);
        this.throwExceptionIfUserCategoryNotFound(categoryUser, userStoreDTO.category_id());
        User user = UserMapper.toEntityStore(userStoreDTO, categoryUser);
        Wallet wallet = this.walletService.createABlankWallet(user);
        user.setWallet(wallet);
        return this.userRepository.save(user);
    }

    /**
     * Verifica se a categoria de usuário não foi encontrada.
     *
     * @param categoryUser A categoria de usuário a ser verificada.
     * @param id O id da categoria de usuário a ser verificada.
     * @throws CategoryUserNotFoundException Se a categoria de usuário não for encontrada.
     */
    private void throwExceptionIfUserCategoryNotFound(CategoryUser categoryUser, Long id){
        if (categoryUser == null){
            throw new CategoryUserNotFoundException(
                    "User category with the ID " + id + " was not found.",
                    HttpStatus.NOT_FOUND
            );
        }
    }

    private void throwExceptionIfUserAlreadyExist(@Valid UserStoreDTO userStoreDTO){
        List<User> userExist = this.userRepository.findByCpfCnpjOrEmail(
                userStoreDTO.cpf_cnpj(),
                userStoreDTO.email()
        );
        if (!userExist.isEmpty()){
            throw new UserAlreadyExistsException(
                    "A user with this email or CPF/CNPJ already exists.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void throwExceptionIfEmailInUseForAnotherUser(@Valid UserUpdateDTO userUpdateDTO, Long id){
        List<User> userExist = this.userRepository.existsByEmailInAnotherUser(
                userUpdateDTO.email(),
                id
        );
        if (!userExist.isEmpty()){
            throw new EmailInUseForAnotherUser(
                    "This email is in use for another user.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Transactional
    public User update(Long id, @Valid UserUpdateDTO userUpdateDTO) {
        User user = this.userRepository.findById(id).orElse(null);
        this.throwExceptionIfUserNotFound(user, id);

        CategoryUser categoryUser =  this.categoryUserRepository.findById(userUpdateDTO.category_id()).orElse(null);
        this.throwExceptionIfUserCategoryNotFound(categoryUser, userUpdateDTO.category_id());

        this.throwExceptionIfEmailInUseForAnotherUser(userUpdateDTO, id);

        User userUpdatedFields = UserMapper.toEntityUpdate(
                userUpdateDTO,
                user,
                categoryUser
        );

        return this.userRepository.save(userUpdatedFields);
    }

    @Transactional
    public void delete(Long id) {
        User user = this.userRepository.findById(id).orElse(null);
        this.throwExceptionIfUserNotFound(user, id);
        this.userRepository.delete(user);
    }
}
