package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CannotTransferMoneyToThemselvesExceptionTest {

    @Test
    public void testThrowDefaultMessage() {
        CannotTransferMoneyToThemselvesException exception = assertThrows(
                CannotTransferMoneyToThemselvesException.class,
                CannotTransferMoneyToThemselvesException::throwDefaultMessage
        );

        assert(exception.getMessage()).equals("A user cannot transfer money to themselves.");
        assert(exception.getHttpStatus()).equals(HttpStatus.BAD_REQUEST);
    }
}
