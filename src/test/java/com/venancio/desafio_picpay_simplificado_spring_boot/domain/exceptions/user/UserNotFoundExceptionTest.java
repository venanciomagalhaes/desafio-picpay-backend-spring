package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserNotFoundExceptionTest {

    @Test
    public void testThrowDefaultMessage() {
        UUID userId = UUID.randomUUID();
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> UserNotFoundException.throwDefaultMessage(userId)
        );

        assert(exception.getMessage()).equals("User with the ID "+ userId +" was not found.");
        assert(exception.getHttpStatus()).equals(HttpStatus.NOT_FOUND);
    }
}
