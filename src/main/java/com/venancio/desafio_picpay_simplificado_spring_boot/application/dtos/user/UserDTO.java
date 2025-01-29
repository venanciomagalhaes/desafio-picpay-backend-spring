package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers.UserController;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.HateoasDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.wallet.WalletDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
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
public class UserDTO extends EntityModel<CategoryUser> implements HateoasDTO<UserDTO> {

    private Long id;

    private String name;

    private String cpf_cnpj;

    private String email;

    private WalletDTO wallet;

    private CategoryUserDTO category;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @Override
    public UserDTO generateHateoas() {
            this.add(
                    linkTo(methodOn(UserController.class).show(this.getId())).withSelfRel()
            );
            this.add(
                        linkTo(methodOn(UserController.class).index(null)).withRel("index")
                );
            this.add(
                        linkTo(methodOn(UserController.class).store(null)).withRel("store")
                );
            this.add(
                        linkTo(methodOn(UserController.class).update(this.getId(), null)).withRel("update")
                );
            this.add(
                        linkTo(methodOn(UserController.class).delete(this.getId())).withRel("delete")
                );
            return this;
    }
}
