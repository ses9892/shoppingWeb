package com.store.project.application.Handling.exception.product;

import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductNotSaleException extends RuntimeException {

    public final static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public final static String code = ResponseDataStatus.BAD_REQUEST;

    public ProductNotSaleException(String meg) {
        super(meg);
    }

    public ProductNotSaleException() {
        super("해당 상품은 현재 판매중지 중인 상품입니다.");
    }
}
