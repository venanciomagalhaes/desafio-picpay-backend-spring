package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
