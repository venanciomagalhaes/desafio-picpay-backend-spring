package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)

public class UnauthorizedTransferException extends BusinessException {

    public UnauthorizedTransferException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static void throwDefaultMessage() {
        throw new UnauthorizedTransferException(
                "Authorization failed: Unauthorized transfer",
                HttpStatus.UNAUTHORIZED
        );
    }
}
