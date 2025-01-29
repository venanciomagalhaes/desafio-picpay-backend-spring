package com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers.CategoryUserMapper;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.response.ResponseBuilder;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.CategoryUserService;
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


@RestController
@RequestMapping("/api/v1/categories-users")
public class CategoryUserController {

    private final CategoryUserService categoryUserService;

    @Autowired
    public CategoryUserController(CategoryUserService categoryUserService) {
        this.categoryUserService = categoryUserService;
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> index(Pageable pageable) {
        Page<CategoryUser> paginatedCategoryUsers = this.categoryUserService.index(pageable);
        List<CategoryUserDTO> categoryUsersDTOList = CategoryUserMapper.toLisDTO(paginatedCategoryUsers.getContent());
        HttpStatus status = categoryUsersDTOList.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseBuilder(
                "User categories listed successfully!",
                status
        )
        .setPagination(paginatedCategoryUsers)
        .setData(categoryUsersDTOList)
        .build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> show(@PathVariable(name = "id") Long id) {
        CategoryUser categoryUser = this.categoryUserService.show(id);
        CategoryUserDTO categoryUserDTO = CategoryUserMapper.toDTO(categoryUser);
        return new ResponseBuilder(
                "User category show successfully!",
                HttpStatus.OK
        )
        .setData(categoryUserDTO)
        .build();
    }


    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> store(@RequestBody @Valid CategoryUserStoreDTO categoryUserStoreDTO) {
        CategoryUser categoryUserListDTO = this.categoryUserService.store(categoryUserStoreDTO);
        CategoryUserDTO categoryUserDTO = CategoryUserMapper.toDTO(categoryUserListDTO);
        return new ResponseBuilder(
                "User category created successfully!",
                HttpStatus.CREATED
        )
        .setData(categoryUserDTO)
        .build();
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid CategoryUserUpdateDTO categoryUserUpdateDTO) {
        CategoryUser categoryUserListDTO = this.categoryUserService.update(id, categoryUserUpdateDTO);
        CategoryUserDTO categoryUserDTO = CategoryUserMapper.toDTO(categoryUserListDTO);
        return new ResponseBuilder(
                "User category updated successfully!",
                HttpStatus.OK
        )
        .setData(categoryUserDTO)
        .build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> delete(@PathVariable(name = "id") Long id) {
        this.categoryUserService.delete(id);
        return new ResponseBuilder(
                "User category deleted successfully!",
                HttpStatus.OK
        )
        .build();
    }
}
