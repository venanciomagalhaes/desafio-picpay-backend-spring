package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.AuthorizationDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.http_client.HttpClient;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.transfer.UnauthorizedTransferException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_ONE_TENTH_SECOND = 100;

    private final HttpClient utilDeviToolsClient;

    @Autowired
    public AuthorizationService(HttpClient utilDeviToolsClient) {
        this.utilDeviToolsClient = utilDeviToolsClient;
    }

    /**
     * Verifica se a transferência está autorizada por um sistema externo.
     *
     * @throws UnauthorizedTransferException Se a transferência não for autorizada.
     */
    public void verifyTransferAuthorization() {
        AuthorizationDTO authorizationDTO = null;
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            try {
                authorizationDTO = this.utilDeviToolsClient.get("/v2/authorize", AuthorizationDTO.class);
                if (authorizationDTO != null && authorizationDTO.getData().isAuthorized()) {
                    break;
                }
            } catch (RuntimeException ignored) {
            } finally {
                attempt++;
                addDelay(attempt);
            }
        }
        if (authorizationDTO == null || !authorizationDTO.getData().isAuthorized()){
            UnauthorizedTransferException.throwDefaultMessage();
        }
    }

    /**
     * Adiciona um atraso entre as tentativas de envio de notificação.
     *
     * @param attempt O número da tentativa atual.
     */
    private void addDelay(int attempt) {
        if (attempt < MAX_ATTEMPTS) {
            try {
                Thread.sleep(AuthorizationService.BACKOFF_ONE_TENTH_SECOND);
            } catch (InterruptedException ignored) {
            }
        }
    }

}
