package com.store.project.application.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData {
    private String code;
    private HttpStatus status;
    private String message;
    private Object item;

}
