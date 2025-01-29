package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM tb_users u WHERE u.cpf_cnpj = :cpfCnpj OR u.email = :email", nativeQuery = true)
    List<User> findByCpfCnpjOrEmail(@Param("cpfCnpj") String cpfCnpj, @Param("email") String email);

    @Query(value = "SELECT u FROM tb_users u WHERE u.email = :email AND u.id != :id", nativeQuery = true)
    List<User> existsByEmailInAnotherUser(@Param("email") String email, @Param("id") Long id);

}
