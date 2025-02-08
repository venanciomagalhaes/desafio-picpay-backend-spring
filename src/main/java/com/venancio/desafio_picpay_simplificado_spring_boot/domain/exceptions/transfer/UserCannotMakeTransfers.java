package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class UserCannotMakeTransfers extends BusinessException {

    public UserCannotMakeTransfers(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static void throwDefaultMessage(UUID id) {
        throw new UserCannotMakeTransfers(
                String.format("User with ID %s cannot make transfers.", id.toString()),
                HttpStatus.BAD_REQUEST
        );
    }
}
