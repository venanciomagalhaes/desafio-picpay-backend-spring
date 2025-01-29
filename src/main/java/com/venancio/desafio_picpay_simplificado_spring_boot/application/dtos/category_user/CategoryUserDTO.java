package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers.CategoryUserController;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.HateoasDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryUserDTO  extends EntityModel<CategoryUser> implements HateoasDTO<CategoryUserDTO>  {

    private Long id;

    private CategoryUserNameEnum name;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @Override
    public CategoryUserDTO generateHateoas() {
        this.add(
                linkTo(methodOn(CategoryUserController.class).show(this.getId())).withSelfRel()
        );
        this.add(
                linkTo(methodOn(CategoryUserController.class).index(null)).withRel("index")
        );
        this.add(
                linkTo(methodOn(CategoryUserController.class).store(null)).withRel("store")
        );
        this.add(
                linkTo(methodOn(CategoryUserController.class).update(this.getId(), null)).withRel("update")
        );
        this.add(
                linkTo(methodOn(CategoryUserController.class).delete(this.getId())).withRel("delete")
        );
        return this;
    }

}
