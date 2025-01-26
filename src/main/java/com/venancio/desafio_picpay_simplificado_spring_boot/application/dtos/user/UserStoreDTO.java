package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_category.user_category_exist.UserCategoryExist;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.cpf_cnpj.CpfCnpj;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.strong_password.StrongPassword;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserStoreDTO(

        @NotEmpty(message = "The name field is required")
        @Length(max = 255, min = 5, message = "The name must be between 5 and 255 characters")
        String name,
        @NotEmpty(message = "The cpf_cnpj field is required")
        @Length(message = "The cpf_cnpj field must contain 11 digits (CPF) or 14 digits (CNPJ)")
        @CpfCnpj(message = "The cpf_cnpj field is invalid")
        String cpf_cnpj,
        @NotEmpty(message = "The email field is required")
        @Email(message = "The email field is invalid")
        String email,
        @NotEmpty(message = "The password field is required")
        @Length(max = 255, min = 5, message = "The password must be between 5 and 255 characters")
        @StrongPassword
        String password,
        @NotNull(message = "The category_id field is required")
        @UserCategoryExist()
        Long category_id
) {

        //TODO: criptografar a senha

        public static User toEntity(UserStoreDTO dto, CategoryUser categoryUser) {
        return new User(
               null,
                dto.name,
                dto.cpf_cnpj,
                dto.email,
                dto.password,
               null,
               categoryUser,
               null,
               null
        );
    }
}
