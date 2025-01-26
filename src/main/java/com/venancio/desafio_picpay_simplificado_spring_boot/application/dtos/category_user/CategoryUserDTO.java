package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers.CategoryUserController;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * DTO (Data Transfer Object) para a entidade {@link CategoryUser}.
 * Esta classe também adiciona links HATEOAS para facilitar a navegação pela API.
 * @author Venâncio
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryUserDTO extends EntityModel<CategoryUser> {

    /**
     * Identificador único da categoria do usuário.
     */
    private Long id;

    /**
     * Nome da categoria do usuário, baseado no enum {@link CategoryUserNameEnum}.
     */
    private CategoryUserNameEnum name;

    /**
     * Data e hora de criação da entidade.
     */
    private LocalDateTime created_at;

    /**
     * Data e hora da última atualização da entidade.
     */
    private LocalDateTime updated_at;

    /**
     * Converte uma entidade {@link CategoryUser} para seu correspondente DTO.
     *
     * @param entities A entidade {@link CategoryUser} a ser convertida.
     * @return Uma instância de {@link CategoryUserDTO} contendo os dados da entidade e os links HATEOAS.
     */
    public static CategoryUserDTO toDTO(CategoryUser entities) {
        CategoryUserDTO categoryUserDTO = new CategoryUserDTO(
                entities.getId(),
                entities.getName(),
                entities.getCreatedAt(),
                entities.getUpdatedAt()
        );
        return generateHateoas(categoryUserDTO);
    }

    /**
     * Converte uma lista de entidades {@link CategoryUser} para uma lista de seus correspondentes DTOs.
     *
     * @param entities Lista de entidades {@link CategoryUser} a ser convertida.
     * @return Lista de {@link CategoryUserDTO} contendo os dados das entidades e os links HATEOAS.
     */
    public static List<CategoryUserDTO> toLisDTO(List<CategoryUser> entities) {
        List<CategoryUserDTO> categoriesUsersListDTOList = new ArrayList<>();
        entities.forEach(categoryUser -> categoriesUsersListDTOList.add(toDTO(categoryUser)));
        return categoriesUsersListDTOList;
    }

    /**
     * Gera os links HATEOAS para a instância do DTO.
     *
     * @param categoryUserDTO O DTO que receberá os links HATEOAS.
     * @return O DTO com os links HATEOAS adicionados.
     */
    protected static CategoryUserDTO generateHateoas(CategoryUserDTO categoryUserDTO) {
        categoryUserDTO.add(
                linkTo(methodOn(CategoryUserController.class).show(categoryUserDTO.getId())).withSelfRel()
        );
        categoryUserDTO.add(
                linkTo(methodOn(CategoryUserController.class).index(null)).withRel("index")
        );
        categoryUserDTO.add(
                linkTo(methodOn(CategoryUserController.class).store(null)).withRel("store")
        );
        categoryUserDTO.add(
                linkTo(methodOn(CategoryUserController.class).update(categoryUserDTO.getId(), null)).withRel("update")
        );
        categoryUserDTO.add(
                linkTo(methodOn(CategoryUserController.class).delete(categoryUserDTO.getId())).withRel("delete")
        );
        return categoryUserDTO;
    }
}
