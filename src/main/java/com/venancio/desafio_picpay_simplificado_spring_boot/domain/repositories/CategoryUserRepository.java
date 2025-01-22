package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  CategoryUserRepository extends JpaRepository<CategoryUser, Long> {

    Optional<CategoryUser> findByName(CategoryUserNameEnum name);

}
