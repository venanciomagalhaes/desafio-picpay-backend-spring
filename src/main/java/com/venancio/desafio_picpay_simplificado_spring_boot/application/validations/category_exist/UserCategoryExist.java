package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.category_exist;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = UserCategoryExistValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserCategoryExist {

    String message() default "User category with the ID was not found.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
