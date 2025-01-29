package com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.TransactionDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.TransactionStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Transaction;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.TransferStatus;

public class TransactionMapper {

    public static Transaction toEntityStore(TransactionStoreDTO dto, User payer, User payee, TransferStatus status) {
       return new Transaction(
            null,
            payer,
            payee,
            dto.getValue(),
            status,
            null,
            null
       );
    }

    public static TransactionDTO toDto(Transaction transaction) {
        return new TransactionDTO(
            UserMapper.toDTO(transaction.getPayer()),
            UserMapper.toDTO(transaction.getPayee()),
            transaction.getValue()
        );
    }
}
