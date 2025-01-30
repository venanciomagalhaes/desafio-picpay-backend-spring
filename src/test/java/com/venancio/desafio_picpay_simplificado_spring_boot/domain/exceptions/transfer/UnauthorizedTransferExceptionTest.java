package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnauthorizedTransferExceptionTest {

    @Test
    public void testThrowDefaultMessage() {
        UnauthorizedTransferException exception = assertThrows(
                UnauthorizedTransferException.class,
                UnauthorizedTransferException::throwDefaultMessage
        );

        assert(exception.getMessage()).equals("Authorization failed: Unauthorized transfer");
        assert(exception.getHttpStatus()).equals(HttpStatus.UNAUTHORIZED);
    }
}
