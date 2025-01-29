package com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.wallet.WalletDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Wallet;

public class WalletMapper {

    public static WalletDTO toDTO(Wallet entity) {
        return new WalletDTO(
                entity.getBalance()
        );
    }
}
