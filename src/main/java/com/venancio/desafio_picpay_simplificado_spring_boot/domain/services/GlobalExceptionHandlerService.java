package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço responsável pelo tratamento de exceções globais, especificamente exceções de validação.
 * Ele processa os erros de validação e os formata em uma lista de mapas de erros.
 *
 * @author  Venâncio
 */
@Service
public class GlobalExceptionHandlerService {

    /**
     * Trata exceções de validação e mapeia os erros para uma lista de mensagens de erro.
     *
     * @param exception A exceção de validação lançada pela aplicação.
     * @return Uma lista de mapas, onde cada mapa contém o nome do campo e a respectiva mensagem de erro.
     */
    public List<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("field", fieldName);
            errorMap.put("instructions", errorMessage);
            errors.add(errorMap);
        });
        return errors;
    }

    public List<Map<String, String>> handleValidationConstraintsExceptions(ConstraintViolationException exception) {
        List<Map<String, String>> errors = new ArrayList<>();
        String input = exception.getMessage();
        String[] parts = input.split(":");
        Map<String, String> errorMap = new HashMap<>();
        String key = parts[0].trim().split("\\.")[1];
        String value = parts[1].trim();
        errorMap.put("field", key);
        errorMap.put("instructions", value);
        errors.add(errorMap);
        return errors;
    }


}
