package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class InsufficientBalanceException extends BusinessException {

    public InsufficientBalanceException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static void throwDefaultMessage(){
        throw new InsufficientBalanceException(
                "The payer's balance is insufficient to complete the transfer.",
                HttpStatus.BAD_REQUEST
        );
    }
}
