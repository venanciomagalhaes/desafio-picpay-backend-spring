package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.email;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class EmailNotificationFailedException extends BusinessException {

    public EmailNotificationFailedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
