package com.store.project.application.Handling.exception.store;

import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;

public class StoreOverFlowPageException extends RuntimeException{

    public final static HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    public final static String code = ResponseDataStatus.NOT_FOUND;

    public StoreOverFlowPageException(String message) {
        super(message);
    }
}
