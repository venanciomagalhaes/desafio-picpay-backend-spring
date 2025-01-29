package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.enums.EnumValid;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_category.user_category_not_exist.UserCategoryNotExistByName;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import jakarta.validation.constraints.NotEmpty;


public record CategoryUserUpdateDTO(
        @NotEmpty(message = "The name field is required")
        @UserCategoryNotExistByName
        @EnumValid(enumClass = CategoryUserNameEnum.class, message = "Invalid value for enum: [common, store]")
        String name
) {


}
