package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserCannotMakeTransfersTest {

    @Test
    public void testThrowDefaultMessage() {
        UUID userId = UUID.randomUUID();
        UserCannotMakeTransfers exception = assertThrows(
                UserCannotMakeTransfers.class,
                () -> UserCannotMakeTransfers.throwDefaultMessage(userId)
        );

        assert(exception.getMessage()).equals("User with ID "+ userId +" cannot make transfers.");
        assert(exception.getHttpStatus()).equals(HttpStatus.BAD_REQUEST);
    }
}
