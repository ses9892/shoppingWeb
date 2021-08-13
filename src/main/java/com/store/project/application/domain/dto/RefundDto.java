package com.store.project.application.domain.dto;

import com.store.project.application.domain.entity.Refund;
import lombok.Data;

import java.util.Date;

@Data
public class RefundDto {

    private Long idx;

    private Long sale_idx;

    private Date date;

    private String reason;

    private Long product_idx;

    private Long store_idx;

    private String client_userId;

    public void setRefundData(Refund refund){
        this.sale_idx = refund.getSale().getIdx();
        this.product_idx = refund.getProduct().getIdx();
        this.store_idx = refund.getStore().getS_idx();
        this.client_userId = refund.getClient().getUserId();
    }
}
