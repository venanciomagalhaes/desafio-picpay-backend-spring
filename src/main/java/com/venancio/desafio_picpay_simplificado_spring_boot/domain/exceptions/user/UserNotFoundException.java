package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static void throwDefaultMessage(UUID id) {
        throw new UserNotFoundException(
                "User with the ID " + id + " was not found.",
                HttpStatus.NOT_FOUND
        );
    }
}
