package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.enum_validation.EnumValid;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import jakarta.validation.constraints.NotEmpty;

/**
 * DTO (Data Transfer Object) para atualizar dados de categorias de usuários.
 *
 * Este DTO é utilizado para validar e transferir dados durante a atualização de uma categoria de usuário existente.
 *
 * @param name O nome da categoria de usuário. Deve ser um valor válido do enum {@link CategoryUserNameEnum}.
 *              - O campo é obrigatório.
 *              - Deve corresponder a um dos valores definidos no enum.
 *              - Valores permitidos: common, store.
 *
 * @author Venâncio
 */
public record CategoryUserUpdateDTO(
        @NotEmpty(message = "The name field is required")
        @EnumValid(enumClass = CategoryUserNameEnum.class, message = "Invalid value for enum: [common, store]")
        String name
) {
    /**
     * Converte um {@link CategoryUserUpdateDTO} em uma entidade {@link CategoryUser}.
     *
     * @param dto O DTO contendo os dados a serem convertidos.
     * @return Uma instância de {@link CategoryUser} contendo os dados convertidos.
     *
     * Apenas o nome da categoria será definido na entidade. Outros campos podem ser ajustados posteriormente
     * no fluxo da aplicação.
     */
    public static CategoryUser toEntity(CategoryUserUpdateDTO dto) {
        return new CategoryUser(
                CategoryUserNameEnum.valueOf(dto.name)
        );
    }
}
