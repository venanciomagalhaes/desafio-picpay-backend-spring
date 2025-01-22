package com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.enum_validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador personalizado para verificar se um valor de string pertence a um conjunto de valores de um enum.
 *
 * Esta classe implementa a interface {@link ConstraintValidator} e é usada em conjunto com a anotação {@link EnumValid}.
 *
 * Funciona validando se o valor fornecido está presente nos valores definidos pela classe de enum associada.
 *
 * @author Venâncio
 */
public class EnumValidator implements ConstraintValidator<EnumValid, String> {

    private Class<? extends Enum<?>> enumClass;

    /**
     * Inicializa o validador com a classe de enum fornecida pela anotação {@link EnumValid}.
     *
     * @param constraintAnnotation A anotação {@link EnumValid} que contém os metadados de validação.
     */
    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    /**
     * Verifica se o valor fornecido é válido.
     *
     * O valor é considerado válido se corresponder a um dos valores definidos na classe de enum.
     *
     * @param value O valor de string a ser validado.
     * @param context O contexto da validação.
     * @return {@code true} se o valor for válido; caso contrário, {@code false}.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Object[] enumConstants = enumClass.getEnumConstants();
            for (Object enumConstant : enumConstants) {
                if (enumConstant.toString().equals(value)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
