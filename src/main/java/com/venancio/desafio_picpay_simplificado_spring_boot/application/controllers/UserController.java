package com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers.UserMapper;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.response.ResponseBuilder;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> index(Pageable pageable) {
        Page<User> usersWithPagination =  this.userService.index(pageable);
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


    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> show(@PathVariable(name = "id") Long id) {
        User user = this.userService.show(id);
        UserDTO userDTO = UserMapper.toDTO(user);
        return new ResponseBuilder(
                "User show successfully!",
                HttpStatus.OK
        )
                .setData(userDTO)
                .build();
    }


    @PostMapping()
    public ResponseEntity<Map<String, Object>> store(@Valid  @RequestBody UserStoreDTO userStoreDTO) {
        User user = this.userService.store(userStoreDTO);
        UserDTO userDTO = UserMapper.toDTO(user);
        return new ResponseBuilder(
                "User created successfully!",
                HttpStatus.CREATED
        )
        .setData(userDTO)
        .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable(name = "id") Long id,
            @Valid  @RequestBody UserUpdateDTO userUpdateDTO
            ) {
        User user = this.userService.update(id, userUpdateDTO);
        UserDTO userDTO = UserMapper.toDTO(user);
        return new ResponseBuilder(
                "User updated successfully!",
                HttpStatus.OK
        )
                .setData(userDTO)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable(name = "id") Long id) {
        this.userService.delete(id);
        return new ResponseBuilder(
                "User deleted successfully!",
                HttpStatus.OK
        )
        .build();
    }
}
