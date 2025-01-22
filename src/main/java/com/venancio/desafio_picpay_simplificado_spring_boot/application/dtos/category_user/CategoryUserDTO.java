package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;

import java.time.LocalDateTime;

public record CategoryUserDTO(
      Long id,
      CategoryUserNameEnum name,
      LocalDateTime created_at,
      LocalDateTime updated_at
) {
    public static CategoryUserDTO toDTO(CategoryUser entities){
        return  new CategoryUserDTO(
                entities.getId(),
                entities.getName(),
                entities.getCreatedAt(),
                entities.getUpdatedAt()
        );
    }
}
