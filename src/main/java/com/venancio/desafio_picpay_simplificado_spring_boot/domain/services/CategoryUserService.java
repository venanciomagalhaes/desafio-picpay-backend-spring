package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user.CategoryUserAlreadyExistsException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user.CategoryUserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de negócios relacionada à entidade {@link CategoryUser}.
 * Contém métodos para criar, atualizar, listar e excluir categorias de usuários.
 *
 * @author Venâncio
 */
@Service
public class CategoryUserService {

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    /**
     * Recupera uma página de todas as categorias de usuário.
     *
     * @param pageable Objeto que define a paginação das categorias de usuário.
     * @return Uma página contendo as categorias de usuário.
     */
    public Page<CategoryUser> index(Pageable pageable){
        return this.categoryUserRepository.findAll(pageable);
    }

    /**
     * Verifica se a categoria de usuário já existe.
     *
     * @param categoryUser A categoria de usuário a ser verificada.
     * @param dto Objeto contendo os dados da categoria a ser validada.
     * @throws CategoryUserAlreadyExistsException Se a categoria de usuário já existir.
     */
    private void throwExceptionIfCategoryUserAlreadyExist(CategoryUserStoreDTO dto) {
        CategoryUser categoryUser = this.categoryUserRepository.findByName(
                CategoryUserNameEnum.valueOf(dto.name())
        ).orElse(null);
        if (categoryUser != null) {
            throw new CategoryUserAlreadyExistsException(
                    "There is already a user category with the name " + dto.name(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * Cria uma nova categoria de usuário.
     *
     * @param dto Objeto com os dados da categoria de usuário a ser criada.
     * @return A categoria de usuário criada.
     * @throws CategoryUserAlreadyExistsException Se a categoria de usuário já existir.
     */
    public CategoryUser store(CategoryUserStoreDTO dto) {
        this.throwExceptionIfCategoryUserAlreadyExist(dto);
        return this.categoryUserRepository.save(CategoryUserStoreDTO.toEntity(dto));
    }


    /**
     * Recupera uma categoria de usuário pelo ID.
     *
     * @param id O ID da categoria de usuário a ser recuperada.
     * @return A categoria de usuário encontrada.
     * @throws CategoryUserNotFoundException Se a categoria de usuário não for encontrada.
     */
    public CategoryUser show(Long id) {
        return this.categoryUserRepository.findById(id)
                .orElseThrow(() -> new CategoryUserNotFoundException("User category with the ID " + id + " was not found.",
                        HttpStatus.NOT_FOUND));
    }

    /**
     * Atualiza uma categoria de usuário existente.
     *
     * @param id O ID da categoria de usuário a ser atualizada.
     * @param categoryUserUpdateDTO Objeto com os dados atualizados da categoria de usuário.
     * @return A categoria de usuário atualizada.
     * @throws CategoryUserNotFoundException Se a categoria de usuário não for encontrada.
     */
    public CategoryUser update(Long id, CategoryUserUpdateDTO categoryUserUpdateDTO) {
        CategoryUser categoryUser = this.categoryUserRepository.findById(id)
                .orElseThrow(() -> new CategoryUserNotFoundException("User category with the ID " + id + " was not found.",
                        HttpStatus.NOT_FOUND));
        CategoryUser categoryUserUpdatedFields = CategoryUserUpdateDTO.toEntity(
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
    public void delete(Long id) {
        CategoryUser categoryUser = this.categoryUserRepository.findById(id)
         .orElseThrow(() -> new CategoryUserNotFoundException("User category with the ID " + id + " was not found.",
                HttpStatus.NOT_FOUND));
        this.categoryUserRepository.delete(categoryUser);
    }
}
