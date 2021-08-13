package com.store.project.application.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class RequestSaleData {

    @NotNull(message = "상품번호를 입력해주세요.")
    private int p_idx;

    @NotNull(message = "구매하실 상품의 갯수를 입력해주세요.")
    @Min(value = 1,message = "최소 1개이상 선택하셔야 합니다.")
    private int ea;

    private String category;

    private int price;
}
