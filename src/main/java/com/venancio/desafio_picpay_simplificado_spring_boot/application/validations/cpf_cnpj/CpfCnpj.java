package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.cpf_cnpj;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CpfCnpjValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CpfCnpj {

    String message() default "Invalid CPF or CNPJ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
