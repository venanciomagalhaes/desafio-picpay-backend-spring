package com.venancio.desafio_picpay_simplificado_spring_boot.application.utils.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe utilitária para construir respostas padronizadas em APIs.
 *
 * Através desta classe, é possível definir mensagens, dados, erros de validação e paginação
 * em um formato consistente para respostas HTTP.
 * <br>
 * <br>
 * Exemplos de uso:
 * <pre>
 * ResponseBuilder responseBuilder = new ResponseBuilder("Operação bem-sucedida", HttpStatus.OK)
 *         .setData(dados)
 *         .setPagination(paginacao)
 *         .setValidationErrors(errosDeValidacao);
 * ResponseEntity&lt;Map&lt;String, Object&gt;&gt; resposta = responseBuilder.build();
 * </pre>
 *
 * @author Venâncio
 */
public class ResponseBuilder {

    private final String message;
    private final HttpStatus httpStatus;
    private Object data;
    private List<Map<String, String>> validationErrors;
    private Object pagination;

    /**
     * Construtor para inicializar a mensagem e o status HTTP.
     *
     * @param message A mensagem descritiva da resposta.
     * @param httpStatus O status HTTP correspondente à resposta.
     */
    public ResponseBuilder(String message, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    /**
     * Define os dados da resposta.
     *
     * @param data O objeto a ser incluído no campo "data" da resposta.
     * @return A instância atual de {@link ResponseBuilder}.
     */
    public ResponseBuilder setData(Object data) {
        this.data = data;
        return this;
    }

    /**
     * Define os erros de validação da resposta.
     *
     * @param validationErrors Lista de mapas contendo os erros de validação.
     * @return A instância atual de {@link ResponseBuilder}.
     */
    public ResponseBuilder setValidationErrors(List<Map<String, String>> validationErrors) {
        this.validationErrors = validationErrors;
        return this;
    }

    /**
     * Define os dados de paginação da resposta.
     *
     * @param pagination Objeto contendo informações de paginação.
     * @return A instância atual de {@link ResponseBuilder}.
     */
    public ResponseBuilder setPagination(Object pagination) {
        this.pagination = pagination;
        return this;
    }

    private Map<String, Object> getSortDetails(Page<?> page) {
        Map<String, Object> sortDetails = new HashMap<>();
        sortDetails.put("sorted", page.getSort().isSorted());
        sortDetails.put("empty", page.getSort().isEmpty());
        sortDetails.put("unsorted", page.getSort().isUnsorted());
        return sortDetails;
    }

    private Map<String, Object> generatePaginationDetails() {

        Page<?> page = (Page<?>) this.pagination;
        Map<String, Object> paginationDetails = new HashMap<>();
        paginationDetails.put("last", page.isLast());
        paginationDetails.put("totalPages", page.getTotalPages());
        paginationDetails.put("totalElements", page.getTotalElements());
        paginationDetails.put("size", page.getSize());
        paginationDetails.put("page_number", page.getNumber());

        Map<String, Object> sortDetails = new HashMap<>();
        sortDetails.put("sorted", page.getSort().isSorted());
        sortDetails.put("empty", page.getSort().isEmpty());
        sortDetails.put("unsorted", page.getSort().isUnsorted());

        paginationDetails.put("sort", sortDetails);
        paginationDetails.put("first", page.isFirst());
        paginationDetails.put("numberOfElements", page.getNumberOfElements());
        paginationDetails.put("empty", page.isEmpty());

        return paginationDetails;
    }

    /**
     * Monta o corpo da resposta em um mapa estruturado.
     *
     * @return Um mapa contendo os dados formatados para a resposta.
     */
    private Map<String, Object> mountResponse() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", this.message);

        if (this.validationErrors != null) {
            response.put("validation_errors", this.validationErrors);
        }

        if (this.data != null) {
            response.put("data", this.data);
        }

        if (this.pagination != null && this.pagination instanceof Page) {
                response.put("pagination", this.generatePaginationDetails());
        }
        return response;
    }

    /**
     * Constrói a resposta completa em um {@link ResponseEntity}.
     *
     * @return Uma instância de {@link ResponseEntity} contendo o corpo e o status HTTP.
     */
    public ResponseEntity<Map<String, Object>> build() {
        return ResponseEntity.status(this.httpStatus).body(this.mountResponse());
    }
}
