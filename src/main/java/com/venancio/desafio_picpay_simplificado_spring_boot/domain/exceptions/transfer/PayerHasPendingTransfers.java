package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class PayerHasPendingTransfers extends BusinessException {

    public PayerHasPendingTransfers(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
