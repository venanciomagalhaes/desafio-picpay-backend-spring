package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PayerHasPendingTransfersTest {

    @Test
    public void testThrowDefaultMessage() {
        PayerHasPendingTransfers exception = assertThrows(
                PayerHasPendingTransfers.class,
                PayerHasPendingTransfers::throwDefaultMessage
        );

        assert(exception.getMessage()).equals("The payer has pending transfers and cannot initiate a new one until they are resolved.");
        assert(exception.getHttpStatus()).equals(HttpStatus.BAD_REQUEST);
    }
}
