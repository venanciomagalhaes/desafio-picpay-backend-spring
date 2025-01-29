package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_exist;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = UserExistValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserExist {

    String message() default "User not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
