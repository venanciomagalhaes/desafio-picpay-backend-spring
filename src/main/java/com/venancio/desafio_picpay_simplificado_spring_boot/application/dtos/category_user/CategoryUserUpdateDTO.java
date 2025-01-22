package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record CategoryUserUpdateDTO(
        @NotEmpty(message = "The name field is required")
        @Length(message = "The name field must contain at least 5 characters and at most 255",
                min = 5, max = 255)
        String name
) {
    public static CategoryUser toEntity(CategoryUserUpdateDTO dto){
        return new CategoryUser(
                null,
                dto.name,
                null,
                null,
                null
        );
    }
}
