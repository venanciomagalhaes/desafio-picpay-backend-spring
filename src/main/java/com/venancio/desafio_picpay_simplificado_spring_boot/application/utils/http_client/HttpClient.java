package com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.http_client;

import org.springframework.http.*;

public interface HttpClient {

    public <T> T get(String uri, Class<T> responseType);

    public <T> T post(String uri, String json, Class<T> responseType);

    public <T> T put(String uri, String json, Class<T> responseType);

    public <T> T delete(String uri, Class<T> responseType);

    public void setStatus(HttpStatusCode status);

    public HttpStatusCode getStatus();
}
