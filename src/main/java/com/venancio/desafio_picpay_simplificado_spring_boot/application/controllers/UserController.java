package com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers.UserMapper;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.response.ResponseBuilder;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.Regex;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(
        name = "Usuários",
        description = "Endpoints para gerenciar usuários"
)
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna uma lista paginada de todos os usuários"
    )
    public ResponseEntity<Map<String, Object>> index(Pageable pageable) {
        Page<User> usersWithPagination = this.userService.index(pageable);
        HttpStatus httpStatus = usersWithPagination.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        List<UserDTO> userDTOList = UserMapper.toLisDTO(usersWithPagination.getContent());
        return new ResponseBuilder(
                "Users listed successfully",
                httpStatus
        )
                .setData(userDTOList)
                .setPagination(usersWithPagination)
                .build();
    }

    @GetMapping(path = "/{id}")
    @Operation(
            summary = "Buscar um usuário pelo ID",
            description = "Retorna os detalhes de um usuário específico pelo ID"
    )
    public ResponseEntity<Map<String, Object>> show(
            @Pattern(regexp = Regex.VALID_REGEX_TO_UUID, message = "The ID must be a valid UUID")
            @PathVariable(name = "id") String id) {
        User user = this.userService.show(UUID.fromString(id));
        UserDTO userDTO = UserMapper.toDTO(user);
        return new ResponseBuilder(
                "User show successfully!",
                HttpStatus.OK
        )
                .setData(userDTO)
                .build();
    }


    @PostMapping()
    @Operation(
            summary = "Criar um novo usuário",
            description = "Cria um novo usuário e retorna seus detalhes"
    )
    public ResponseEntity<Map<String, Object>> store(@Valid @RequestBody UserStoreDTO userStoreDTO) {
        User user = this.userService.store(userStoreDTO);
        UserDTO userDTO = UserMapper.toDTO(user);
        return new ResponseBuilder(
                "User created successfully!",
                HttpStatus.CREATED
        )
                .setData(userDTO)
                .build();
    }


    @PutMapping(path = "/{id}")
    @Operation(
            summary = "Atualizar um usuário",
            description = "Atualiza os detalhes de um usuário pelo ID"
    )
    public ResponseEntity<Map<String, Object>> update(
            @Pattern(regexp = Regex.VALID_REGEX_TO_UUID, message = "The ID must be a valid UUID")
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        User user = this.userService.update(UUID.fromString(id), userUpdateDTO);
        UserDTO userDTO = UserMapper.toDTO(user);
        return new ResponseBuilder(
                "User updated successfully!",
                HttpStatus.OK
        )
                .setData(userDTO)
                .build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(
            summary = "Excluir um usuário",
            description = "Exclui um usuário pelo ID"
    )
    public ResponseEntity<Map<String, Object>> delete(
            @Pattern(regexp = Regex.VALID_REGEX_TO_UUID, message = "The ID must be a valid UUID")
            @PathVariable(name = "id") String id) {
        this.userService.delete(UUID.fromString(id));
        return new ResponseBuilder(
                "User deleted successfully!",
                HttpStatus.OK
        )
                .build();
    }
}
