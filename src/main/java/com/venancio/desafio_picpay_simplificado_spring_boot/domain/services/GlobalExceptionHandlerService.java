package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GlobalExceptionHandlerService {

    public List<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("campo", fieldName);
            errorMap.put("instrucao", errorMessage);
            errors.add(errorMap);
        });
        return errors;
    }
}
