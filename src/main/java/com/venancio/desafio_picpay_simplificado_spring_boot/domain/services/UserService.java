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
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Serviço de gerenciamento de usuários no sistema.
 * Fornece métodos para criar, ler, atualizar e excluir usuários,
 * além de verificar condições de validação relacionadas aos usuários e categorias.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CategoryUserRepository categoryUserRepository;
    private final WalletService walletService;

    /**
     * Construtor do serviço de usuário.
     *
     * @param userRepository         Repositório de usuários.
     * @param categoryUserRepository Repositório de categorias de usuário.
     * @param walletService          Serviço de carteiras de usuários.
     */
    @Autowired
    public UserService(UserRepository userRepository, CategoryUserRepository categoryUserRepository, WalletService walletService) {
        this.userRepository = userRepository;
        this.categoryUserRepository = categoryUserRepository;
        this.walletService = walletService;
    }

    /**
     * Retorna uma lista paginada de usuários.
     *
     * @param pageable Informações de paginação.
     * @return Página de usuários.
     */
    public Page<User> index(Pageable pageable){
        return this.userRepository.findAll(pageable);
    }

    /**
     * Retorna um usuário específico com base no ID fornecido.
     *
     * @param id ID do usuário a ser retornado.
     * @return Usuário com o ID fornecido.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    public User show(UUID id) {
        User user = this.userRepository.findById(id).orElse(null);
        this.throwExceptionIfUserNotFound(user, id);
        return user;
    }

    /**
     * Lança uma exceção se o usuário com o ID fornecido não for encontrado.
     *
     * @param user O usuário a ser verificado.
     * @param id   O ID do usuário.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    private void throwExceptionIfUserNotFound(User user, UUID id){
        if (user == null){
           UserNotFoundException.throwDefaultMessage(id);
        }
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param userStoreDTO Dados de entrada para criar o usuário.
     * @return O usuário criado.
     * @throws UserAlreadyExistsException Se o usuário já existir com o mesmo CPF ou email.
     * @throws CategoryUserNotFoundException Se a categoria do usuário não for encontrada.
     */
    @Transactional
    public User store(@Valid UserStoreDTO userStoreDTO) {
        this.throwExceptionIfUserAlreadyExist(userStoreDTO);
        CategoryUser categoryUser = this.categoryUserRepository.findById(UUID.fromString(userStoreDTO.category_id())).orElse(null);
        this.throwExceptionIfUserCategoryNotFound(categoryUser, UUID.fromString(userStoreDTO.category_id()));
        User user = UserMapper.toEntityStore(userStoreDTO, categoryUser);
        this.userRepository.save(user);
        Wallet wallet = this.walletService.createABlankWallet(user);
        user.setWallet(wallet);
        return this.userRepository.save(user);
    }

    /**
     * Lança uma exceção se a categoria do usuário não for encontrada.
     *
     * @param categoryUser A categoria de usuário a ser verificada.
     * @param id O ID da categoria.
     * @throws CategoryUserNotFoundException Se a categoria do usuário não for encontrada.
     */
    private void throwExceptionIfUserCategoryNotFound(CategoryUser categoryUser, UUID id){
        if (categoryUser == null){
            CategoryUserNotFoundException.throwDefaultMessage(id);
        }
    }

    /**
     * Lança uma exceção se o CPF ou email do usuário já estiver em uso.
     *
     * @param userStoreDTO Dados de entrada do novo usuário.
     * @throws UserAlreadyExistsException Se já existir um usuário com o mesmo CPF ou email.
     */
    private void throwExceptionIfUserAlreadyExist(@Valid UserStoreDTO userStoreDTO){
        List<User> userExist = this.userRepository.findByCpfCnpjOrEmail(
                userStoreDTO.cpf_cnpj(),
                userStoreDTO.email()
        );
        if (!userExist.isEmpty()){
            UserAlreadyExistsException.throwDefaultMessage();
        }
    }

    /**
     * Lança uma exceção se o email fornecido já estiver em uso por outro usuário.
     *
     * @param userUpdateDTO Dados de atualização do usuário.
     * @param id            ID do usuário a ser atualizado.
     * @throws EmailInUseForAnotherUser Se o email já estiver em uso por outro usuário.
     */
    private void throwExceptionIfEmailInUseForAnotherUser(@Valid UserUpdateDTO userUpdateDTO, UUID id){
        List<User> userExist = this.userRepository.existsByEmailInAnotherUser(
                userUpdateDTO.email(),
                id
        );
        if (!userExist.isEmpty()){
           EmailInUseForAnotherUser.throwDefaultMessage();
        }
    }

    /**
     * Atualiza os dados de um usuário existente no sistema.
     *
     * @param id ID do usuário a ser atualizado.
     * @param userUpdateDTO Dados de atualização do usuário.
     * @return O usuário atualizado.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     * @throws CategoryUserNotFoundException Se a categoria do usuário não for encontrada.
     * @throws EmailInUseForAnotherUser Se o email fornecido já estiver em uso por outro usuário.
     */
    @Transactional
    public User update(UUID id, @Valid UserUpdateDTO userUpdateDTO) {
        User user = this.userRepository.findById(id).orElse(null);
        this.throwExceptionIfUserNotFound(user, id);

        CategoryUser categoryUser =  this.categoryUserRepository.findById(UUID.fromString(userUpdateDTO.category_id())).orElse(null);
        this.throwExceptionIfUserCategoryNotFound(categoryUser, UUID.fromString(userUpdateDTO.category_id()));

        this.throwExceptionIfEmailInUseForAnotherUser(userUpdateDTO, id);

        User userUpdatedFields = UserMapper.toEntityUpdate(
                userUpdateDTO,
                user,
                categoryUser
        );

        return this.userRepository.save(userUpdatedFields);
    }

    /**
     * Exclui um usuário do sistema com base no ID fornecido.
     *
     * @param id ID do usuário a ser excluído.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    @Transactional
    public void delete(UUID id) {
        User user = this.userRepository.findById(id).orElse(null);
        this.throwExceptionIfUserNotFound(user, id);
        this.userRepository.delete(user);
    }
}
