package com.store.project.application.Handling.exception.client;

import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;

public class OwnerIsNotException extends RuntimeException{


    public final static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public final static String code = ResponseDataStatus.BAD_REQUEST;
    public final static String meg = "해당 상품의 사업자만 접근할 수 있습니다.";

    public OwnerIsNotException() {
        super(meg);
    }

    public OwnerIsNotException(String message) {
        super(message);
    }
}
