package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


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
        Exception exception = assertThrows(CategoryUserNotFoundException.class, () ->
                CategoryUserNotFoundException.throwDefaultMessage(1L));

        assertEquals("User category with the ID 1 was not found.", exception.getMessage());
    }
}
