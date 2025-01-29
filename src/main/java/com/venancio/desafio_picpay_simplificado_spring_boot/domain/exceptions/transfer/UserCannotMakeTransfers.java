package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class UserCannotMakeTransfers extends BusinessException {

    public UserCannotMakeTransfers(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
