package com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers.UserController;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.wallet.WalletDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserMapper {

    public static UserDTO toDTO(User entity) {
        UserDTO userDTO = new UserDTO(
                entity.getId(),
                entity.getName(),
                entity.getCpfCnpj(),
                entity.getEmail(),
                WalletMapper.toDTO(entity.getWallet()),
                CategoryUserMapper.toDTO(entity.getCategory()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
        return userDTO.generateHateoas();
    }


    public static List<UserDTO> toLisDTO(List<User> entities) {
        List<UserDTO> userList = new ArrayList<>();
        entities.forEach(user -> userList.add(toDTO(user)));
        return userList;

    }

    public static User toEntityStore(UserStoreDTO dto, CategoryUser categoryUser) {
        return new User(
                dto.name(),
                dto.cpf_cnpj(),
                dto.email(),
                dto.password(),
                categoryUser
        );
    }

    public static User toEntityUpdate(UserUpdateDTO dto, User user, CategoryUser categoryUser) {
            if (dto.category_id() != null){
                user.setCategory(categoryUser);
            }

            if (dto.name() != null && !dto.name().isBlank()) {
                user.setName(dto.name());
            }

            if (dto.email() != null && !dto.email().isBlank()) {
                user.setEmail(dto.email());
            }

            if (dto.password() != null && !dto.password().isBlank()){
                user.setPassword(dto.password());
            }

            return user;
    }

}
