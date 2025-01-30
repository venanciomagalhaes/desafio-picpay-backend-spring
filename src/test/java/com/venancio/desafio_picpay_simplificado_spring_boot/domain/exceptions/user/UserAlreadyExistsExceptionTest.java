package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserAlreadyExistsExceptionTest {

    @Test
    public void testThrowDefaultMessage() {
        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                UserAlreadyExistsException::throwDefaultMessage
        );

        assert(exception.getMessage()).equals("A user with this email or CPF/CNPJ already exists.");
        assert(exception.getHttpStatus()).equals(HttpStatus.BAD_REQUEST);
    }
}
