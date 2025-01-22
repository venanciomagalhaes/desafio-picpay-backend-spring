package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class CategoryUserAlreadyExistsException extends BusinessException {

    public CategoryUserAlreadyExistsException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
