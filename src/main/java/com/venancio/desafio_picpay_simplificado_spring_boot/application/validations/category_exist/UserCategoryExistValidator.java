package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.category_exist;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserCategoryExistValidator implements ConstraintValidator<UserCategoryExist, Long> {

    @Autowired
    private CategoryUserRepository categoryUserRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        try {
            if (value == null) {
                return true;
            }
            Optional<CategoryUser> categoryUser = this.categoryUserRepository.findById(value);
            return categoryUser.isPresent();
        } catch (Exception e) {
            return false;
        }
    }
}
