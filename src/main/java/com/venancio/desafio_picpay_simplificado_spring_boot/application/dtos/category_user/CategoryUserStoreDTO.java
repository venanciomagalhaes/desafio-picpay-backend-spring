package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.enums.EnumValid;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_category.user_category_not_exist.UserCategoryNotExistByName;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import jakarta.validation.constraints.NotEmpty;

/**
 * DTO (Data Transfer Object) para armazenar dados de categorias de usuários.
 * Utilizado para validar e transferir dados durante a criação de uma categoria de usuário.
 *
 * Este DTO utiliza validações para garantir que os dados fornecidos sejam válidos antes de serem
 * processados pela aplicação.
 *
 * @param name O nome da categoria de usuário. Deve ser um valor válido do enum {@link CategoryUserNameEnum}.
 *              - O campo é obrigatório.
 *              - Deve corresponder a um dos valores definidos no enum.
 *              - Valores permitidos: common, store.
 *
 * @author Venâncio
 */
public record CategoryUserStoreDTO(
        @NotEmpty(message = "The name field is required")
        @EnumValid(enumClass = CategoryUserNameEnum.class, message = "Invalid value for enum: [common, store]")
        @UserCategoryNotExistByName
        String name
) {
    /**
     * Converte um {@link CategoryUserStoreDTO} em uma entidade {@link CategoryUser}.
     *
     * @param dto O DTO contendo os dados a serem convertidos.
     * @return Uma instância de {@link CategoryUser} contendo os dados convertidos.
     *
     * O ID da entidade será gerado automaticamente pelo banco de dados.
     * As datas de criação e atualização serão geradas automaticamente.
     */
    public static CategoryUser toEntity(CategoryUserStoreDTO dto) {
        return new CategoryUser(
                CategoryUserNameEnum.valueOf(dto.name)
        );
    }
}
