package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class InsufficientBalanceException extends BusinessException {

    public InsufficientBalanceException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
