package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
