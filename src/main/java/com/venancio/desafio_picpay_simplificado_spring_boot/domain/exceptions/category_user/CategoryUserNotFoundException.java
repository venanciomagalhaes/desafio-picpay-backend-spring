package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryUserNotFoundException extends BusinessException {

    public CategoryUserNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static void throwDefaultMessage(Long id){
        throw new CategoryUserNotFoundException(
                "User category with the ID " + id + " was not found.",
                HttpStatus.NOT_FOUND
        );
    }
}
