package com.store.project.application.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.store.project.application.domain.entity.Client;
import com.store.project.application.domain.entity.Product;
import com.store.project.application.domain.entity.Store;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class SaleDto {

    private int idx;

    private String category;

    private Date date;

    private int ea;

    private int price;

    private int product_idx;

    private int store_idx;

    private String client_userId;

    private String commit;

    public void setProductAndStoreAndClient(Product product){
        this.product_idx = Math.toIntExact(product.getIdx());
        this.store_idx = Math.toIntExact(product.getStore().getS_idx());
        this.client_userId = product.getStore().getClient().getUserId();
    }


}
