package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.strong_password.StrongPassword;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_category.user_category_exist.UserCategoryExist;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UUID;

public record UserUpdateDTO(
        @Length(max = 255, min = 5, message = "The name must be between 5 and 255 characters")
        String name,
        @Email(message = "The email field is invalid")
        String email,
        @Length(max = 255, min = 5, message = "The password must be between 5 and 255 characters")
        @StrongPassword
        String password,
        @UserCategoryExist()
        @UUID
        String category_id
) {

}
