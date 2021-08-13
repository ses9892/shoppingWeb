package com.store.project.application.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ResponseException {

    private String code;
    private HttpStatus status;
    private String exception;
    private String message;
    private Object item;
}
