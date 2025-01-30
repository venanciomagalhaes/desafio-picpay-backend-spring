package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class CannotTransferMoneyToThemselvesException extends BusinessException {

    public CannotTransferMoneyToThemselvesException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static void throwDefaultMessage(){
        throw new CannotTransferMoneyToThemselvesException(
                "A user cannot transfer money to themselves.",
                HttpStatus.BAD_REQUEST
        );
    }
}
