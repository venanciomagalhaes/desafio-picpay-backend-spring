package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Transaction;
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
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Override
    @EntityGraph(attributePaths = {"payee", "payer"})
    Page<Transaction> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"payer", "payee"})
    @Query("SELECT t FROM Transaction t WHERE t.status = 'pending' AND t.payer.id = :id")
    List<Transaction> findPendingTransfersWithPayerUser(@Param("id") UUID id);
}
