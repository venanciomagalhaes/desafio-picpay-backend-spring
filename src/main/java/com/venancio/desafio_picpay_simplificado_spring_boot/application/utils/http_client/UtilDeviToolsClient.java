package com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.http_client;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UtilDeviToolsClient implements HttpClient {

    private final String server = "https://util.devi.tools/api";
    private final RestTemplate rest;
    private final HttpHeaders headers;

    private HttpStatusCode status;

    public UtilDeviToolsClient(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.rest = restTemplate;
        this.headers = httpHeaders;
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    public <T> T get(String uri, Class<T> responseType) {
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        ResponseEntity<T> responseEntity = rest.exchange(
                server + uri, HttpMethod.GET, requestEntity, responseType);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public <T> T post(String uri, String json, Class<T> responseType) {
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        ResponseEntity<T> responseEntity = rest.exchange(
                server + uri, HttpMethod.POST, requestEntity, responseType);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public <T> T put(String uri, String json, Class<T> responseType) {
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        ResponseEntity<T> responseEntity = rest.exchange(
                server + uri, HttpMethod.PUT, requestEntity, responseType);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public <T> T delete(String uri, Class<T> responseType) {
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        ResponseEntity<T> responseEntity = rest.exchange(
                server + uri, HttpMethod.DELETE, requestEntity, responseType);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    @Override
    public void setStatus(HttpStatusCode status) {
        this.status = status;
    }

    @Override
    public HttpStatusCode getStatus() {
        return this.status;
    }
}
