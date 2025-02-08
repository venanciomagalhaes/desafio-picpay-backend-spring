package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.can_send_transfer.CanSendTransfer;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_exist.UserExist;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStoreDTO {

    @NotNull
    @Min(0)
    private BigDecimal value;

    @NotNull
    @UserExist
    @CanSendTransfer
    @org.hibernate.validator.constraints.UUID(message = "The ID must be a valid UUID")
    private String payer;

    @NotNull
    @UserExist
    @org.hibernate.validator.constraints.UUID(message = "The ID must be a valid UUID")
    private String payee;

}

