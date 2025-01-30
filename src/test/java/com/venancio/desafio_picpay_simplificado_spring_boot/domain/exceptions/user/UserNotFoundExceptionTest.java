package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserNotFoundExceptionTest {

    @Test
    public void testThrowDefaultMessage() {
        Long userId = 123L;
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> UserNotFoundException.throwDefaultMessage(userId)
        );

        assert(exception.getMessage()).equals("User with the ID 123 was not found.");
        assert(exception.getHttpStatus()).equals(HttpStatus.NOT_FOUND);
    }
}
