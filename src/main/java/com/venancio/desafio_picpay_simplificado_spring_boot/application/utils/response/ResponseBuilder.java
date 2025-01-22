package com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResponseBuilder {

    private final String message;
    private final HttpStatus httpStatus;
    private Object data;
    private List<Map<String, String>> validationErrors;
    private Object pagination;

    public ResponseBuilder(String message, HttpStatus httpStatus){
        this.httpStatus = httpStatus;
        this.message = message;
    }


    public ResponseBuilder setData(Object data) {
        this.data = data;
        return this;
    }

    public ResponseBuilder setValidationErrors(List<Map<String, String>> validationErrors) {
        this.validationErrors = validationErrors;
        return this;
    }

    public ResponseBuilder setPagination(Object pagination) {
        this.pagination = pagination;
        return this;
    }

    private Map<String, Object>  mountResponse(){
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", this.message);
        if (this.validationErrors != null){
            response.put("validation_errors",   this.validationErrors);
        }
        response.put("data",this.data == null ? new HashMap<String, String>() : this.data);
        if (this.pagination != null){
            response.put("pagination", this.pagination);
        }
        return response;
    }

    public ResponseEntity<Map<String, Object>> build(){
        return ResponseEntity.status(this.httpStatus).body(this.mountResponse());
    }
}
