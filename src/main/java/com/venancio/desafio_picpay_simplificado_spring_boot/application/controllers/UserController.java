package com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.response.ResponseBuilder;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador responsável por gerenciar as requisições relacionadas aos usuários.
 *
 * Este controlador oferece endpoints para listar usuários, visualizar detalhes de um usuário específico
 * e criar um novo usuário.
 *
 * Autor: Venâncio
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint responsável por listar os usuários com paginação.
     *
     * @param pageable objeto Pageable para paginar os resultados.
     * @return ResponseEntity contendo a lista de usuários com status HTTP adequado.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> index(Pageable pageable) {
        Page<User> usersWithPagination =  this.userService.index(pageable);
        HttpStatus httpStatus = usersWithPagination.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        List<UserDTO> userDTOList = UserDTO.toLisDTO(usersWithPagination.getContent());
        return new ResponseBuilder(
                "Users listed successfully",
                httpStatus
        )
        .setData(userDTOList)
        .setPagination(usersWithPagination)
        .build();
    }

    /**
     * Endpoint responsável por exibir os detalhes de um usuário específico.
     *
     * @param id ID do usuário a ser visualizado.
     * @return ResponseEntity contendo os dados do usuário com status HTTP 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> show(@PathVariable(name = "id") Long id) {
        User user = this.userService.show(id);
        UserDTO userDTO = UserDTO.toDTO(user);
        return new ResponseBuilder(
                "User show successfully!",
                HttpStatus.OK
        )
                .setData(userDTO)
                .build();
    }

    /**
     * Endpoint responsável por criar um novo usuário.
     *
     * @param userStoreDTO Dados do usuário a ser criado. Recebidos no corpo da requisição.
     * @return ResponseEntity contendo os dados do usuário criado com status HTTP 200.
     */
    @PostMapping()
    @Transactional
    public ResponseEntity<Map<String, Object>> store(@Valid  @RequestBody UserStoreDTO userStoreDTO) {
        User user = this.userService.store(userStoreDTO);
        UserDTO userDTO = UserDTO.toDTO(user);
        return new ResponseBuilder(
                "User created successfully!",
                HttpStatus.CREATED
        )
        .setData(userDTO)
        .build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable(name = "id") Long id,
            @Valid  @RequestBody UserUpdateDTO userUpdateDTO
            ) {
        User user = this.userService.update(id, userUpdateDTO);
        UserDTO userDTO = UserDTO.toDTO(user);
        return new ResponseBuilder(
                "User updated successfully!",
                HttpStatus.OK
        )
                .setData(userDTO)
                .build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> delete(@PathVariable(name = "id") Long id) {
        this.userService.delete(id);
        return new ResponseBuilder(
                "User deleted successfully!",
                HttpStatus.OK
        )
        .build();
    }
}
