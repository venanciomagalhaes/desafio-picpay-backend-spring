package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para acesso e manipulação de dados relacionados à entidade {@link CategoryUser}.
 * Esta interface estende {@link JpaRepository} e fornece métodos adicionais para consulta
 * de categorias de usuários no banco de dados.
 *
 * @author Venâncio
 */
@Repository
public interface CategoryUserRepository extends JpaRepository<CategoryUser, Long> {

    List<CategoryUser> findByName(CategoryUserNameEnum name);

}
