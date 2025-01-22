package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.enum_validation.EnumValid;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record CategoryUserUpdateDTO(
        @NotEmpty(message = "The name field is required")
        @EnumValid(enumClass = CategoryUserNameEnum.class, message = "Invalid value for enum: [common, store]")
        String name
) {
    public static CategoryUser toEntity(CategoryUserUpdateDTO dto){
        return new CategoryUser(
                null,
                CategoryUserNameEnum.valueOf(dto.name),
                null,
                null,
                null
        );
    }
}
