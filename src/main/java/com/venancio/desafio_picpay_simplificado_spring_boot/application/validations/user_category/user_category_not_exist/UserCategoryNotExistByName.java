package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_category.user_category_not_exist;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = UserCategoryNotExistValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserCategoryNotExistByName {

    String message() default "User category with the name already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
