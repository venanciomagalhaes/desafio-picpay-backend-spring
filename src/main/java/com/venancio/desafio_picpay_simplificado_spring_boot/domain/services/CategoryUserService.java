package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user.CategoryUserAlreadyExistsException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user.CategoryUserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CategoryUserService {

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    public Page<CategoryUser> index(Pageable pageable){
        return this.categoryUserRepository.findAll(pageable);
    }

    private void verifyCategoryUserAlreadyExists(CategoryUser categoryUser, CategoryUserStoreDTO dto) {
        if (categoryUser != null) {
            throw new CategoryUserAlreadyExistsException(
                    "There is already a user category with the name " + dto.name(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public CategoryUser store(CategoryUserStoreDTO dto) {
        CategoryUser categoryUserExist = this.categoryUserRepository.findByName(
                CategoryUserNameEnum.valueOf(dto.name())
        ).orElse(null);
        this.verifyCategoryUserAlreadyExists(categoryUserExist, dto);
        return this.categoryUserRepository.save(CategoryUserStoreDTO.toEntity(dto));
    }

    private void verifyCategoryUserNotFound(CategoryUser categoryUser, Long id){
        if (categoryUser == null){
            throw new CategoryUserNotFoundException(
                    "User category with the ID " + id + " was not found.",
                    HttpStatus.NOT_FOUND
            );
        }
    }

    public CategoryUser show(Long id) {
        CategoryUser categoryUser = this.categoryUserRepository.findById(id).orElse(null);
        this.verifyCategoryUserNotFound(categoryUser, id);
        return categoryUser;
    }

    public CategoryUser update(Long id, @Valid CategoryUserUpdateDTO categoryUserUpdateDTO) {
        CategoryUser categoryUser = this.categoryUserRepository.findById(id).orElse(null);
        this.verifyCategoryUserNotFound(categoryUser, id);
        categoryUser.setName(CategoryUserNameEnum.valueOf(categoryUserUpdateDTO.name()));
        return this.categoryUserRepository.save(categoryUser);
    }

    public void delete(Long id) {
        CategoryUser categoryUser = this.categoryUserRepository.findById(id).orElse(null);
        this.verifyCategoryUserNotFound(categoryUser, id);
        this.categoryUserRepository.delete(categoryUser);
    }

}
