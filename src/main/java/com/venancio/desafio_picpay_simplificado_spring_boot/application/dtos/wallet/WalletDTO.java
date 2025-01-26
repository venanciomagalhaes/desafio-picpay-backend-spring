package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.wallet;

import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Wallet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletDTO  {

    private BigDecimal balance;

    public static WalletDTO toDTO(Wallet entity) {
        return new WalletDTO(
            entity.getBalance()
        );
    }


    public static List<WalletDTO> toLisDTO(List<Wallet> entities) {
        List<WalletDTO> walletDTOList = new ArrayList<>();
        entities.forEach(wallet -> walletDTOList.add(toDTO(wallet)));
        return walletDTOList;
    }

}
