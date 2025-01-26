package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers.CategoryUserController;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers.UserController;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.wallet.WalletDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Wallet;
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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO extends EntityModel<CategoryUser> {

    private Long id;

    private String name;

    private String cpf_cnpj;

    private String email;

    private WalletDTO wallet;

    private CategoryUserNameEnum category;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;


    public static UserDTO toDTO(User entity) {
        UserDTO userDTO = new UserDTO(
               entity.getId(),
                entity.getName(),
                entity.getCpfCnpj(),
                entity.getEmail(),
                WalletDTO.toDTO(entity.getWallet()),
                CategoryUserNameEnum.valueOf(entity.getCategory().getName().name()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
        return generateHateoas(userDTO);
    }


    public static List<UserDTO> toLisDTO(List<User> entities) {
        List<UserDTO> userList = new ArrayList<>();
        entities.forEach(user -> userList.add(toDTO(user)));
        return userList;

    }

    private static UserDTO generateHateoas(UserDTO userDTO) {
        userDTO.add(
                linkTo(methodOn(UserController.class).show(userDTO.getId())).withSelfRel()
        );
        userDTO.add(
                linkTo(methodOn(UserController.class).index(null)).withRel("index")
        );
        userDTO.add(
                linkTo(methodOn(UserController.class).store(null)).withRel("store")
        );
//        userDTO.add(
//                linkTo(methodOn(UserController.class).update(userDTO.getId(), null)).withRel("update")
//        );
//        userDTO.add(
//                linkTo(methodOn(UserController.class).delete(userDTO.getId())).withRel("delete")
//        );
        return userDTO;
    }
}
