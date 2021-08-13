package com.store.project.application.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.store.project.application.domain.entity.Client;
import com.store.project.application.domain.entity.Product;
import com.store.project.application.domain.entity.Stock;
import com.store.project.application.domain.entity.Store;
import lombok.Data;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreDto {

    private Long s_idx;

    private String stName;

    private String stDelivery;

    private String stBrand;

//    private Client client;
    private String client_id;

    private List<Integer> product_idx_list;

    public void setProductList(List<Product> productList){
        product_idx_list = new ArrayList<>();
        for (Product p :productList) {
            product_idx_list.add(Math.toIntExact(p.getIdx()));
        }
    }



}
