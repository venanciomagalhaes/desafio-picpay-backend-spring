package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private HttpStatus httpStatus;
    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
