package com.store.project.application.Handling.exception;

import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WriterIsNotException extends RuntimeException {


    public final static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public final static String code = ResponseDataStatus.BAD_REQUEST;

    public WriterIsNotException(String message) {
        super(message);
    }
}