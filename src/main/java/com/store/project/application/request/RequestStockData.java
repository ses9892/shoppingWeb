package com.store.project.application.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class RequestStockData {

    @NotNull(message = "재고개수를 입력해주세요")
    @Min(value = 0,message = "최소 개수는 0개 이상이어야 합니다.")
    private int ea;

    @NotNull(message = "판매 여부를 선택해주세요")
    private Boolean flag;
}
