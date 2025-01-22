package com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.response.ResponseBuilder;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.GlobalExceptionHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @Autowired
    private GlobalExceptionHandlerService errorHandlerService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = this.errorHandlerService.handleValidationException(ex);
        return new ResponseBuilder(
                "Os campos enviados estão inválidos",
                HttpStatus.UNPROCESSABLE_ENTITY
        )
        .setValidationErrors(errors)
        .build();

    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        return new ResponseBuilder(
                ex.getMessage(),
                ex.getHttpStatus()
        ).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return new ResponseBuilder(
               "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR
        ).build();
    }
}
