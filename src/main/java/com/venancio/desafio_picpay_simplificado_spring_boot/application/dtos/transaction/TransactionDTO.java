package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.user.UserDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.can_send_transfer.CanSendTransfer;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.validations.user_exist.UserExist;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {

    private UserDTO payer;

    private UserDTO payee;

    private BigDecimal value;

}
