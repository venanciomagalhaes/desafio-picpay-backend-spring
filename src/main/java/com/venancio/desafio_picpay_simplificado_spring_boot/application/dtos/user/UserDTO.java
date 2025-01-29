package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.wallet.WalletDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDateTime;

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

    private CategoryUserDTO category;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
