package com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
