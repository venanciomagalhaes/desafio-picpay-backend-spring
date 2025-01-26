package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_category.user_category_not_exist;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserCategoryNotExistValidator implements ConstraintValidator<UserCategoryNotExistByName, String> {

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if (value == null || value.isEmpty()) {
                return false;
            }
            Optional<CategoryUser> categoryUser = this.categoryUserRepository.findByName(
                    CategoryUserNameEnum.valueOf(value)
            );
            return categoryUser.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
