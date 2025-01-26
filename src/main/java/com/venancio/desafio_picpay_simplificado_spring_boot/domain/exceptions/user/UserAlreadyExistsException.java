package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Exceção personalizada para casos em que uma categoria de usuário já existe no sistema.
 * Esta exceção estende a {@link BusinessException} e permite associar uma mensagem de erro
 * e um status HTTP específico.
 *
 * @author Venâncio
 */
public class UserAlreadyExistsException extends BusinessException {

    /**
     * Construtor para criar uma instância de {@code CategoryUserAlreadyExistsException}.
     *
     * @param message A mensagem de erro a ser associada à exceção.
     * @param httpStatus O status HTTP relacionado ao erro.
     */
    public UserAlreadyExistsException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
