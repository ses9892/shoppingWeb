package com.store.project.application.Handling.exception.product;

import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductEaNotEnoughException extends RuntimeException {

    public final static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public final static String code = ResponseDataStatus.BAD_REQUEST;

    public ProductEaNotEnoughException(String meg) {
        super(meg);
    }

    public ProductEaNotEnoughException() {
        super("상품의 재고가 충분하지 않습니다.");
    }
}
