package com.store.project.application.Handling.exception.sale;

import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SaleNotFoundException extends RuntimeException {

    public final static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public final static String code = ResponseDataStatus.BAD_REQUEST;

    public SaleNotFoundException(String meg) {
        super(meg);
    }

    public SaleNotFoundException() {
        super("판매내역이 존재하지 않거나, 구매 상품이 아닙니다.");
    }
}
