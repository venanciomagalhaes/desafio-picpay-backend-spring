package com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.response.ResponseBuilder;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.GlobalExceptionHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

/**
 * Classe de tratamento global de exceções para a aplicação.
 *
 * O {@link  GlobalExceptionHandlerController} captura e lida com exceções que ocorrem em toda a aplicação.
 * Ele utiliza a anotação {@link ControllerAdvice} para fornecer tratamento centralizado de exceções para
 * todas as camadas da aplicação. Cada método de tratamento de exceção retorna uma resposta personalizada
 * com base no tipo de erro ocorrido.
 *
 * <p>Exceções tratadas:</p>
 * <ul>
 *     <li>{@link MethodArgumentNotValidException} - Falha na validação de dados de entrada.</li>
 *     <li>{@link BusinessException} - Erros relacionados à lógica de negócio da aplicação.</li>
 *     <li>{@link HttpMessageNotReadableException} - Erros ao tentar ler ou desserializar o corpo da requisição.</li>
 *     <li>{@link Exception} - Exceções genéricas ou não tratadas explicitamente.</li>
 * </ul>
 *
 * @author Venâncio
 */

@ControllerAdvice
public class GlobalExceptionHandlerController {

    private final GlobalExceptionHandlerService errorHandlerService;

    @Autowired
    public GlobalExceptionHandlerController(GlobalExceptionHandlerService errorHandlerService) {
        this.errorHandlerService = errorHandlerService;
    }

    /**
     * Captura exceções de validação de campos.
     *
     * Este método é acionado quando ocorre uma falha na validação de parâmetros de entrada. Ele retorna uma resposta com
     * detalhes sobre os campos inválidos e um status HTTP {@code 422 Unprocessable Entity}.
     *
     * @param ex A exceção {@code MethodArgumentNotValidException} que contém detalhes sobre os erros de validação.
     * @return A resposta {@code ResponseEntity} contendo os erros de validação e um status HTTP {@code 422}.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = this.errorHandlerService.handleValidationException(ex);
        return new ResponseBuilder(
                "The fields sent are invalid.",
                HttpStatus.UNPROCESSABLE_ENTITY
        )
                .setValidationErrors(errors)
                .build();
    }

    /**
     * Captura exceções de erro de negócio.
     *
     * Este método é acionado quando ocorre um erro específico relacionado às regras de negócio da aplicação.
     * Ele retorna a mensagem de erro fornecida pela exceção e um status HTTP correspondente.
     *
     * @param ex A exceção {@code BusinessException} que contém a mensagem de erro e o status HTTP associado.
     * @return A resposta {@code ResponseEntity} com a mensagem de erro e o status HTTP associado.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        return new ResponseBuilder(
                ex.getMessage(),
                ex.getHttpStatus()
        ).build();
    }

    /**
     * Captura exceções relacionadas ao corpo da requisição malformado.
     *
     * Este método é acionado quando ocorre um erro ao tentar ler ou desserializar o corpo da requisição,
     * retornando uma resposta com status HTTP {@code 400 Bad Request}.
     *
     * @param ex A exceção {@code HttpMessageNotReadableException} que ocorre quando o corpo da requisição é malformado.
     * @return A resposta {@code ResponseEntity} com a mensagem de erro e o status HTTP {@code 400}.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseBuilder(
                "The request body is malformed or cannot be deserialized correctly.",
                HttpStatus.BAD_REQUEST
        ).build();
    }

    /**
     * Captura exceções genéricas.
     *
     * Este método é acionado quando ocorre uma exceção não específica, garantindo que qualquer erro inesperado seja tratado.
     * Ele retorna uma resposta genérica com a mensagem "Internal server error" e o status HTTP {@code 500 Internal Server Error}.
     *
     * @param ex A exceção {@code Exception} genérica.
     * @return A resposta {@code ResponseEntity} com a mensagem de erro genérico e status {@code 500}.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return new ResponseBuilder(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR
        ).build();
    }
}
