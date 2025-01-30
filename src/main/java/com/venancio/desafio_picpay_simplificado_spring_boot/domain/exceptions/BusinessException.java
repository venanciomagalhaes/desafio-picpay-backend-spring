package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Exceção personalizada para tratar erros de negócios no sistema.
 * Esta exceção permite associar uma mensagem de erro e um status HTTP específico.
 *
 * @author Venâncio
 */

@Getter
@Setter
public class BusinessException extends RuntimeException {

    /**
     * O status HTTP associado à exceção.
     */
    private HttpStatus httpStatus;

    /**
     * Construtor para criar uma instância de {@code BusinessException}.
     *
     * @param message A mensagem de erro a ser associada à exceção.
     * @param httpStatus O status HTTP relacionado ao erro.
     */
    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
