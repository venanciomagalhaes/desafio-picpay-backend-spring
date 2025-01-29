package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_exist;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class UserExistValidator implements ConstraintValidator<UserExist, Long> {

    private final UserRepository userRepository;

    @Autowired
    public UserExistValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        try {
            this.userRepository.findById(value)
                    .orElseThrow(() -> new UserNotFoundException(
                            "User with the ID " + value + " was not found.",
                            HttpStatus.NOT_FOUND
                    ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
