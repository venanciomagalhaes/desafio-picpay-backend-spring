package com.venancio.desafio_picpay_simplificado_spring_boot.domain.services.Transfer;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.dtos.transaction.TransactionStoreDTO;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.http_client.HttpClient;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.entities.User;
import com.venancio.desafio_picpay_simplificado_spring_boot.domain.exceptions.email.EmailNotificationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private static final int MAX_ATTEMPTS = 5;
    private static int BACKOFF_ONE_TENTH_SECOND = 100;

    private final HttpClient utilDeviToolsClient;

    @Autowired
    public NotificationService(HttpClient utilDeviToolsClient) {
        this.utilDeviToolsClient = utilDeviToolsClient;
    }



    /**
     * Envia notificações de email para o pagador e o recebedor sobre o status da transação.
     *
     * @param transactionStoreDTO DTO da transação.
     * @param payee Usuário recebedor da transação.
     * @param payer Usuário pagador da transação.
     * @throws EmailNotificationFailedException Se não for possível enviar as notificações.
     */
    public void sendNotificationsToPayerAndPayee(TransactionStoreDTO transactionStoreDTO, User payee, User payer)  {
        boolean isSend = false;
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            try {
                this.utilDeviToolsClient.post("/v1/notify", null, null);
                isSend = true;
                logger.info("Email de notificação enviado");
                break;
            } catch (Exception ignored) {
                logger.info("Não foi possível enviar o email de notificação");
            } finally {
                attempt++;
                this.addDelay(attempt);
            }
        }
        if (!isSend){
            EmailNotificationFailedException.throwDefaultMessage();
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
                Thread.sleep(NotificationService.BACKOFF_ONE_TENTH_SECOND);
            } catch (InterruptedException ignored) {
            }
        }
    }

}
