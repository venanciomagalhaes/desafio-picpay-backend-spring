package com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers.CategoryUserController;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers.UserController;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.wallet.WalletDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CategoryUserMapper {

    public static CategoryUserDTO toDTO(CategoryUser entities) {
        CategoryUserDTO categoryUserDTO = new CategoryUserDTO(
                entities.getId(),
                entities.getName(),
                entities.getCreatedAt(),
                entities.getUpdatedAt()
        );
        return generateHateoas(categoryUserDTO);
    }


    public static List<CategoryUserDTO> toLisDTO(List<CategoryUser> entities) {
        List<CategoryUserDTO> categoriesUsersListDTOList = new ArrayList<>();
        entities.forEach(categoryUser -> categoriesUsersListDTOList.add(toDTO(categoryUser)));
        return categoriesUsersListDTOList;
    }

    public static CategoryUser toEntityStore(CategoryUserStoreDTO dto) {
        return new CategoryUser(
                CategoryUserNameEnum.valueOf(dto.name())
        );
    }


    public static CategoryUser toEntityUpdate(CategoryUserUpdateDTO dto, CategoryUser categoryUser) {
        if (dto.name() != null && !dto.name().isBlank()){
            categoryUser.setName(CategoryUserNameEnum.valueOf(dto.name()));
        }
        return categoryUser;
    }

    protected static CategoryUserDTO generateHateoas(CategoryUserDTO categoryUserDTO) {
        categoryUserDTO.add(
                linkTo(methodOn(CategoryUserController.class).show(categoryUserDTO.getId())).withSelfRel()
        );
        categoryUserDTO.add(
                linkTo(methodOn(CategoryUserController.class).index(null)).withRel("index")
        );
        categoryUserDTO.add(
                linkTo(methodOn(CategoryUserController.class).store(null)).withRel("store")
        );
        categoryUserDTO.add(
                linkTo(methodOn(CategoryUserController.class).update(categoryUserDTO.getId(), null)).withRel("update")
        );
        categoryUserDTO.add(
                linkTo(methodOn(CategoryUserController.class).delete(categoryUserDTO.getId())).withRel("delete")
        );
        return categoryUserDTO;
    }
}
