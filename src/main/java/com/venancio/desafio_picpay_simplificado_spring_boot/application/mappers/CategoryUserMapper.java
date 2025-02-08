package com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;

import java.util.ArrayList;
import java.util.List;

public class CategoryUserMapper {

    public static CategoryUserDTO toDTO(CategoryUser entities) {
        CategoryUserDTO categoryUserDTO = new CategoryUserDTO(
                entities.getId(),
                entities.getName(),
                entities.getCreatedAt(),
                entities.getUpdatedAt()
        );
        return categoryUserDTO.generateHateoas();
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


}
