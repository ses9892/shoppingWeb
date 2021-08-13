package com.store.project.application.Handling.exception.product;

import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductNotFoundException extends RuntimeException {

    public final static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public final static String code = ResponseDataStatus.BAD_REQUEST;

    public ProductNotFoundException(String meg) {
        super(meg);
    }
}
