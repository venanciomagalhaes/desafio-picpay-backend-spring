package com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.notifications;

public interface EmailNotification {

    public void sendEmail(String to, String subject, String text) throws Exception;
}
