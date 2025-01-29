package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_category.user_category_not_exist;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserCategoryNotExistValidator implements ConstraintValidator<UserCategoryNotExistByName, String> {

    private final CategoryUserRepository categoryUserRepository;

    @Autowired
    public UserCategoryNotExistValidator(CategoryUserRepository categoryUserRepository) {
        this.categoryUserRepository = categoryUserRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if (value == null || value.isEmpty()) {
                return false;
            }
            List<CategoryUser> categoryUser = this.categoryUserRepository.findByName(
                    CategoryUserNameEnum.valueOf(value)
            );
            return categoryUser.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
