package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.can_send_transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user.UserNotFoundException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.CategoryUserRepository;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class CanSendTransferValidator implements ConstraintValidator<CanSendTransfer, Long> {

    private final UserRepository userRepository;

    @Autowired
    public CanSendTransferValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        try {
            User user = this.userRepository.findById(value)
                    .orElseThrow(() -> new UserNotFoundException(
                            "User with the ID " + value + " was not found.",
                            HttpStatus.NOT_FOUND
                    ));
            CategoryUser categoryUser = user.getCategory();
            return CategoryUserNameEnum.common.name().equals(categoryUser.getName().name());
        } catch (Exception e) {
            return false;
        }
    }
}
