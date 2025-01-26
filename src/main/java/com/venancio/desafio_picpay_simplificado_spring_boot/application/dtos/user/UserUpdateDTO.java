package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.strong_password.StrongPassword;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_category.user_category_exist.UserCategoryExist;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record UserUpdateDTO(
        @Length(max = 255, min = 5, message = "The name must be between 5 and 255 characters")
        String name,
        @Email(message = "The email field is invalid")
        String email,
        @Length(max = 255, min = 5, message = "The password must be between 5 and 255 characters")
        @StrongPassword
        String password,
        @UserCategoryExist()
        Long category_id
) {

        public static User toEntity(UserUpdateDTO dto, User user, CategoryUser categoryUser) {
                if (dto.category_id() != null){
                        user.setCategory(categoryUser);
                }

                if (dto.name() != null && !dto.name().isBlank()) {
                        user.setName(dto.name());
                }

                if (dto.email() != null && !dto.email().isBlank()) {
                        user.setEmail(dto.email());
                }

                //TODO: criptografar
                if (dto.password() != null && !dto.password().isBlank()){
                        user.setPassword(dto.password());
                }

                return user;
        }
}
