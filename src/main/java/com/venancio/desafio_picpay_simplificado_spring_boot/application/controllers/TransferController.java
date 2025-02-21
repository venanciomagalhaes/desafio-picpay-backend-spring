package com.venancio.desafio_picpay_simplificado_spring_boot.application.controllers;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.TransactionDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.TransactionStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.mappers.TransactionMapper;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.response.ResponseBuilder;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.Transaction;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.Transfer.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/transfer")
@Tag(name = "Transferências", description = "Endpoints para realizar transferências entre usuários")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping()
    @Operation(
            summary = "Realizar transferência",
            description = "Executa uma transferência entre usuários com base nos dados informados."
    )
    public ResponseEntity<Map<String, Object>> transfer(@Valid @RequestBody TransactionStoreDTO transactionStoreDTO) {
        Transaction transaction = this.transferService.transfer(transactionStoreDTO);
        TransactionDTO transactionDTO = TransactionMapper.toDto(transaction);
        return new ResponseBuilder(
                "Transfer completed successfully",
                HttpStatus.OK
        )
                .setData(transactionDTO)
                .build();
    }
}
