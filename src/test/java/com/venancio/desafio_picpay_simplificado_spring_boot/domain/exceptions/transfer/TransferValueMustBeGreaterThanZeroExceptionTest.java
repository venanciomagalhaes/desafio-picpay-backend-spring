package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransferValueMustBeGreaterThanZeroExceptionTest {

    @Test
    public void testThrowDefaultMessage() {
        TransferValueMustBeGreaterThanZeroException exception = assertThrows(
                TransferValueMustBeGreaterThanZeroException.class,
                TransferValueMustBeGreaterThanZeroException::throwDefaultMessage
        );

        assert(exception.getMessage()).equals("Transfer value must be greater than zero.");
        assert(exception.getHttpStatus()).equals(HttpStatus.BAD_REQUEST);
    }
}
