package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class TransferValueMustBeGreaterThanZeroException extends BusinessException {

    public TransferValueMustBeGreaterThanZeroException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static void throwDefaultMessage(){
        throw new TransferValueMustBeGreaterThanZeroException(
                "Transfer value must be greater than zero.",
                HttpStatus.BAD_REQUEST
        );
    }
}
