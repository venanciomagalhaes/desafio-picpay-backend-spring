package com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoriesUsersListDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.category_user.CategoryUserUpdateDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.response.ResponseBuilder;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.CategoryUserService;
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
public class CategoryUserController  {

    @Autowired
    private CategoryUserService categoryUserService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> index(Pageable pageable) {
        Page<CategoryUser> paginatedCategoryUsers = this.categoryUserService.index(pageable);
        List<CategoriesUsersListDTO> categoryUsersDTOList = CategoriesUsersListDTO.toLisDTO(paginatedCategoryUsers.getContent());
        HttpStatus status = categoryUsersDTOList.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseBuilder(
                "User categories listed successfully!",
                status
        )
        .setPagination(paginatedCategoryUsers.getPageable())
        .setData(categoryUsersDTOList)
        .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> show(@PathVariable(name = "id") Long id) {
       CategoryUser categoryUser = this.categoryUserService.show(id);
       CategoryUserDTO categoryUserDTO = CategoryUserDTO.toDTO(categoryUser);
        return new ResponseBuilder(
                "User category show successfully!",
                HttpStatus.OK
        )
        .setData(categoryUserDTO)
        .build();
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> store(@RequestBody @Valid CategoryUserStoreDTO categoryUserStoreDTO){
        CategoryUser categoryUserListDTO = this.categoryUserService.store(categoryUserStoreDTO);
        CategoryUserDTO categoryUserDTO = CategoryUserDTO.toDTO(categoryUserListDTO);
        return new ResponseBuilder(
                "User category created successfully!",
                HttpStatus.CREATED
        )
        .setData(categoryUserDTO)
        .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable(name = "id") Long id, @RequestBody @Valid CategoryUserUpdateDTO categoryUserUpdateDTO){
        CategoryUser categoryUserListDTO = this.categoryUserService.update(id, categoryUserUpdateDTO);
        CategoryUserDTO categoryUserDTO = CategoryUserDTO.toDTO(categoryUserListDTO);
        return new ResponseBuilder(
                "User category updated successfully!",
                HttpStatus.OK
        )
        .setData(categoryUserDTO)
        .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable(name = "id") Long id){
        this.categoryUserService.delete(id);
        return new ResponseBuilder(
                "User category deleted successfully!",
                HttpStatus.OK
        )
        .build();
    }
}
