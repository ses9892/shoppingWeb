package com.store.project.application.domain.dto;

import lombok.Data;

@Data
public class StockDto {

    private long product_idx;

    private int st_ea;

    private Boolean st_flag;
}
