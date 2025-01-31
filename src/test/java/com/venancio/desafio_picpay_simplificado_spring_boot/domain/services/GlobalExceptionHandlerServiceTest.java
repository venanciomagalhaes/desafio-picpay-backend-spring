package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerServiceTest {

    private GlobalExceptionHandlerService globalExceptionHandlerService;

    @Mock
    private MethodArgumentNotValidException exception;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandlerService = new GlobalExceptionHandlerService();
    }

    @Test
    @DisplayName("Deve mapear erros de validação para uma lista de mapas")
    void handleValidationExceptionTest() {
        // Prepara dados simulados
        FieldError fieldError = new FieldError("user", "name", "Name is required");
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        // Chama o método que está sendo testado
        List<Map<String, String>> result = globalExceptionHandlerService.handleValidationException(exception);

        // Verifica o conteúdo do resultado
        assertNotNull(result);
        assertEquals(1, result.size());

        Map<String, String> errorMap = result.get(0);
        assertEquals("name", errorMap.get("field"));
        assertEquals("Name is required", errorMap.get("instructions"));

        // Verifica se os métodos do Mockito foram chamados
        verify(exception, times(1)).getBindingResult();
        verify(bindingResult, times(1)).getAllErrors();
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver erros de validação")
    void handleNoValidationExceptionTest() {
        // Prepara dados simulados sem erros
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of());

        // Chama o método que está sendo testado
        List<Map<String, String>> result = globalExceptionHandlerService.handleValidationException(exception);

        // Verifica se o resultado é uma lista vazia
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verifica se os métodos do Mockito foram chamados
        verify(exception, times(1)).getBindingResult();
        verify(bindingResult, times(1)).getAllErrors();
    }
}
