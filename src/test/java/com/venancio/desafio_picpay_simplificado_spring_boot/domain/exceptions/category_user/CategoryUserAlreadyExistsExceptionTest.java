package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.category_user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


class CategoryUserAlreadyExistsExceptionTest {

    @Test
    void testExceptionMessageAndStatus() {
        CategoryUserAlreadyExistsException exception = new CategoryUserAlreadyExistsException(
                "Category already exists", HttpStatus.BAD_REQUEST);

        assertEquals("Category already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void testThrowDefaultMessage() {
        Exception exception = assertThrows(CategoryUserAlreadyExistsException.class, () ->
                CategoryUserAlreadyExistsException.throwDefaultMessage("TestCategory"));

        assertEquals("There is already a user category with the name TestCategory", exception.getMessage());
    }
}
