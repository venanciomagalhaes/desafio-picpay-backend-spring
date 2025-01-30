package com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailInUseForAnotherUserTest {

    @Test
    public void testThrowDefaultMessage() {
        EmailInUseForAnotherUser exception = assertThrows(
                EmailInUseForAnotherUser.class,
                EmailInUseForAnotherUser::throwDefaultMessage
        );

        assert(exception.getMessage()).equals("This email is in use for another user.");
        assert(exception.getHttpStatus()).equals(HttpStatus.BAD_REQUEST);
    }
}
