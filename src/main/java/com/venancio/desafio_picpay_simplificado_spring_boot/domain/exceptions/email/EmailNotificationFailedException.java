package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.email;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EmailNotificationFailedException extends BusinessException {

    public EmailNotificationFailedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static void throwDefaultMessage(){
        throw new EmailNotificationFailedException(
                "It was not possible to send the email notifications to the payer and payee.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
