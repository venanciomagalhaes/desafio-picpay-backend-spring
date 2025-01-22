package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.enum_validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValid, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
        Object[] enumConstants = enumClass.getEnumConstants();
        for (Object enumConstant : enumConstants) {
            if (enumConstant.toString().equals(value)) {
                return true;
            }
        }
        return  false;
        } catch (Exception e) {
            return false;
        }
    }
}
