package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Override
    @EntityGraph(attributePaths = {"wallet", "category"})
    Page<User> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"wallet", "category"})
    Optional<User> findById(UUID uuid);

    @EntityGraph(attributePaths = {"wallet", "category"})
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.wallet w " +
            "LEFT JOIN FETCH u.category c " +
            "WHERE u.cpfCnpj = :cpfCnpj OR u.email = :email")
    List<User> findByCpfCnpjOrEmail(@Param("cpfCnpj") String cpfCnpj, @Param("email") String email);

    @EntityGraph(attributePaths = {"wallet", "category"})
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.wallet w " +
            "LEFT JOIN FETCH u.category c " +
            "WHERE u.email = :email AND u.id != :id")
    List<User> existsByEmailInAnotherUser(@Param("email") String email, @Param("id") UUID id);
}
