package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class EmailInUseForAnotherUser extends BusinessException {

    public EmailInUseForAnotherUser(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static void throwDefaultMessage() {
        throw new EmailInUseForAnotherUser(
                "This email is in use for another user.",
                HttpStatus.BAD_REQUEST
        );
    }
}
