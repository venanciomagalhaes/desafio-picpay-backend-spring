package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record CategoriesUsersListDTO(
      Long id,
      String name,
      LocalDateTime created_at,
      LocalDateTime updated_at
) {
    public static List<CategoriesUsersListDTO> toLisDTO(List<CategoryUser> entities){
        List<CategoriesUsersListDTO> categoriesUsersListDTOList = new ArrayList<>();
        entities.forEach(categoryUser -> categoriesUsersListDTOList.add(
                new CategoriesUsersListDTO(
                        categoryUser.getId(),
                        categoryUser.getName(),
                        categoryUser.getCreatedAt(),
                        categoryUser.getUpdatedAt()
                )
        ));
        return categoriesUsersListDTOList;
    }
}
