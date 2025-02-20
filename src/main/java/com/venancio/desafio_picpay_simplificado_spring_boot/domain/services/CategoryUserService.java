package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers.CategoryUserMapper;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user.CategoryUserAlreadyExistsException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user.CategoryUserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócios relacionada à entidade {@link CategoryUser}.
 * Contém métodos para criar, atualizar, listar e excluir categorias de usuários.
 *
 * @author Venâncio
 */
@Service
public class CategoryUserService {

    private final CategoryUserRepository categoryUserRepository;

    /**
     * Construtor da classe {@link CategoryUserService}.
     *
     * @param categoryUserRepository O repositório de {@link CategoryUser} para interações com o banco de dados.
     */
    @Autowired
    public CategoryUserService(CategoryUserRepository categoryUserRepository) {
        this.categoryUserRepository = categoryUserRepository;
    }

    /**
     * Recupera uma página de todas as categorias de usuário.
     *
     * @param pageable Objeto que define a paginação das categorias de usuário.
     * @return Uma página contendo as categorias de usuário.
     */
    public Page<CategoryUser> index(Pageable pageable) {
        return this.categoryUserRepository.findAll(pageable);
    }

    /**
     * Verifica se a categoria de usuário já existe.
     *
     * @param dto Objeto contendo os dados da categoria a ser validada.
     * @throws CategoryUserAlreadyExistsException Se a categoria de usuário já existir.
     */
    private void throwExceptionIfCategoryUserAlreadyExist(@Valid CategoryUserStoreDTO dto) {
        List<CategoryUser> categoryUser = this.categoryUserRepository.findByName(
                CategoryUserNameEnum.valueOf(dto.name())
        );
        if (!categoryUser.isEmpty()) {
            CategoryUserAlreadyExistsException.throwDefaultMessage(dto.name());
        }
    }

    /**
     * Cria uma nova categoria de usuário.
     *
     * @param dto Objeto com os dados da categoria de usuário a ser criada.
     * @return A categoria de usuário criada.
     * @throws CategoryUserAlreadyExistsException Se a categoria de usuário já existir.
     */
    @Transactional
    public CategoryUser store(@Valid CategoryUserStoreDTO dto) {
        this.throwExceptionIfCategoryUserAlreadyExist(dto);
        return this.categoryUserRepository.save(CategoryUserMapper.toEntityStore(dto));
    }

    /**
     * Recupera uma categoria de usuário pelo ID.
     *
     * @param id O ID da categoria de usuário a ser recuperada.
     * @return A categoria de usuário encontrada.
     * @throws CategoryUserNotFoundException Se a categoria de usuário não for encontrada.
     */
    public CategoryUser show(UUID id) {
        CategoryUser categoryUser = this.categoryUserRepository.findById(id).orElse(null);
        this.throwExceptionIfCategoryUserNotFound(id, categoryUser);
        return categoryUser;
    }

    private void throwExceptionIfCategoryUserNotFound(UUID id, CategoryUser categoryUser) {
        if (categoryUser == null){
            CategoryUserNotFoundException.throwDefaultMessage(id);
        }
    }

    /**
     * Atualiza uma categoria de usuário existente.
     *
     * @param id O ID da categoria de usuário a ser atualizada.
     * @param categoryUserUpdateDTO Objeto com os dados atualizados da categoria de usuário.
     * @return A categoria de usuário atualizada.
     * @throws CategoryUserNotFoundException Se a categoria de usuário não for encontrada.
     */
    @Transactional
    public CategoryUser update(UUID id, @Valid CategoryUserUpdateDTO categoryUserUpdateDTO) {
        CategoryUser categoryUser = this.categoryUserRepository.findById(id).orElse(null);
        if (categoryUser == null){
            CategoryUserNotFoundException.throwDefaultMessage(id);
        }
        CategoryUser categoryUserUpdatedFields = CategoryUserMapper.toEntityUpdate(
                categoryUserUpdateDTO,
                categoryUser
        );
        return this.categoryUserRepository.save(categoryUserUpdatedFields);
    }

    /**
     * Exclui uma categoria de usuário pelo ID.
     *
     * @param id O ID da categoria de usuário a ser excluída.
     * @throws CategoryUserNotFoundException Se a categoria de usuário não for encontrada.
     */
    @Transactional
    public void delete(UUID id) {
        CategoryUser categoryUser = this.categoryUserRepository.findById(id).orElse(null);
        this.throwExceptionIfCategoryUserNotFound(id, categoryUser);
        if(categoryUser != null){
            this.categoryUserRepository.delete(categoryUser);
        }
    }
}
