package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryUserRepository extends JpaRepository<CategoryUser, Long> {

    List<CategoryUser> findByName(CategoryUserNameEnum name);

}
