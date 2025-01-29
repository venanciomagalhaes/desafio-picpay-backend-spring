package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {

    @JsonProperty("payer_name")
    private String payerName;

    @JsonProperty("payer_document")
    private String payerCpfCnpj;

    @JsonProperty("payee_name")
    private String payeeName;

    @JsonProperty("payee_document")
    private String payeeCpfCnpj;

    private BigDecimal value;
    private TransferStatus status;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
