package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.enum_validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValid {
    String message() default "Invalid value for enum";
    Class<?>[] groups() default {};
    Class<? extends Enum<?>> enumClass();
    Class<? extends Payload>[] payload() default {};
}
