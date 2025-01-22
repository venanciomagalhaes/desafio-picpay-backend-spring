package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.CategoryUser;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.CategoryUserNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

    /**
     * Método para buscar uma categoria de usuário pelo nome.
     *
     * @param name O nome da categoria de usuário, que deve ser um valor válido da enum {@link CategoryUserNameEnum}.
     * @return Um {@link Optional} contendo a categoria de usuário encontrada, ou vazio caso não seja encontrada.
     */
    Optional<CategoryUser> findByName(CategoryUserNameEnum name);

}
