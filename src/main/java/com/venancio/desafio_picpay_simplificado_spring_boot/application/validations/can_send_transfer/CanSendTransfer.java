package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.can_send_transfer;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = CanSendTransferValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CanSendTransfer {

    String message() default "User is not allowed to send transfers";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
