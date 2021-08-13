package com.store.project.application.Handling.exception.ReView;

import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReViewNotFoundException extends RuntimeException{


    public final static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public final static String code = ResponseDataStatus.BAD_REQUEST;

    public ReViewNotFoundException(String message) {
        super(message);
    }
}
