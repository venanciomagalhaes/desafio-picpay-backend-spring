package com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationDTO {

    private String status;
    private Data data;


    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class Data {
        private boolean authorization;

        public boolean isAuthorized() {
            return authorization;
        }
    }

}
