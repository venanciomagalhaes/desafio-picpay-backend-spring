package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Wallet;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Serviço responsável por gerenciar as carteiras de usuários no sistema.
 * Este serviço permite a criação de carteiras e a interação com os dados persistidos.
 */
@Service
public class WalletService {

    private final WalletRepository walletRepository;

    /**
     * Construtor do serviço de carteira.
     *
     * @param walletRepository Repositório responsável por persistir as carteiras.
     */
    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Cria uma carteira em branco para o usuário fornecido.
     * A carteira será inicializada com um saldo de R$ 1.000,00.
     *
     * @param user O usuário para o qual a carteira será criada.
     * @return A carteira criada para o usuário.
     */
    public Wallet createABlankWallet(User user){
        return this.walletRepository.save(new Wallet(null, BigDecimal.valueOf(1000), user, null, null));
    }
}
