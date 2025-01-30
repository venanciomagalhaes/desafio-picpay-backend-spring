package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.email;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


class EmailNotificationFailedExceptionTest {

    @Test
    void testExceptionMessageAndStatus() {
        EmailNotificationFailedException exception = new EmailNotificationFailedException(
                "Email notification failed", HttpStatus.INTERNAL_SERVER_ERROR);

        assertEquals("Email notification failed", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

    @Test
    void testThrowDefaultMessage() {
        Exception exception = assertThrows(EmailNotificationFailedException.class,
                EmailNotificationFailedException::throwDefaultMessage);

        assertEquals("It was not possible to send the email notifications to the payer and payee.",
                exception.getMessage());
    }
}
