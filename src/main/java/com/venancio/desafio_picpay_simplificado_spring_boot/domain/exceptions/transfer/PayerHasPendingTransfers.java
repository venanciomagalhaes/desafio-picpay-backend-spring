package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class PayerHasPendingTransfers extends BusinessException {

    public PayerHasPendingTransfers(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static void throwDefaultMessage(){
        throw new PayerHasPendingTransfers(
                "The payer has pending transfers and cannot initiate a new one until they are resolved.",
                HttpStatus.BAD_REQUEST
        );
    }
}
