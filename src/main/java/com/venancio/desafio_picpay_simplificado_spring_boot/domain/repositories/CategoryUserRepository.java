package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  CategoryUserRepository extends JpaRepository<CategoryUser, Long> {

    @Query(value = "SELECT * FROM tb_category_users WHERE name = :name", nativeQuery = true)
    Optional<CategoryUser> getCategoryUserByName(String name);

}
