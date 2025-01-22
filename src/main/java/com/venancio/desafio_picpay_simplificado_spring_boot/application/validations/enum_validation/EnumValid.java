package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.enum_validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para validar se um valor pertence a um enum específico.
 *
 * @author Venâncio
 */
@Constraint(validatedBy = EnumValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValid {

    /**
     * Mensagem de erro padrão para validação.
     *
     * @return mensagem de erro.
     */
    String message() default "Invalid value for enum";

    /**
     * Grupos de validação para a anotação.
     *
     * @return grupos de validação.
     */
    Class<?>[] groups() default {};

    /**
     * Classe do enum que será validada.
     *
     * @return classe do enum.
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * Metadados adicionais para a validação.
     *
     * @return metadados de payload.
     */
    Class<? extends Payload>[] payload() default {};
}
