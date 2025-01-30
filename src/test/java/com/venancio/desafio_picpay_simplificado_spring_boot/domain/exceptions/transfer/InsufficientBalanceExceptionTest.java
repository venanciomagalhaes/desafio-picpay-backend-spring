package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class InsufficientBalanceExceptionTest {

    @Test
    public void testThrowDefaultMessage() {
        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                InsufficientBalanceException::throwDefaultMessage
        );

        assert(exception.getMessage()).equals("The payer's balance is insufficient to complete the transfer.");
        assert(exception.getHttpStatus()).equals(HttpStatus.BAD_REQUEST);
    }
}
