package com.venancio.desafio_picpay_simplificado_spring_boot.infrastructure;

import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.http_client.HttpClient;
import com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.http_client.UtilDeviToolsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public HttpHeaders httpHeaders(){
        return new HttpHeaders(

        );
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(

        );
    }


    @Bean
    public HttpClient httpClient(){
        return new UtilDeviToolsClient(
            this.restTemplate(),
            this.httpHeaders()
        );
    }
}
