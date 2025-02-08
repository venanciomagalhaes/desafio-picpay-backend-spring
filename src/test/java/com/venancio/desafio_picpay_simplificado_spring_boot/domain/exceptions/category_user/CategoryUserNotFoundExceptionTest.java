package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;


class CategoryUserNotFoundExceptionTest {

    @Test
    void testExceptionMessageAndStatus() {
        CategoryUserNotFoundException exception = new CategoryUserNotFoundException(
                "User category not found", HttpStatus.NOT_FOUND);

        assertEquals("User category not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testThrowDefaultMessage() {
        UUID random = UUID.randomUUID();
        Exception exception = assertThrows(CategoryUserNotFoundException.class, () ->
                CategoryUserNotFoundException.throwDefaultMessage(random));

        assertEquals("User category with the ID " + random +" was not found.", exception.getMessage());
    }
}
