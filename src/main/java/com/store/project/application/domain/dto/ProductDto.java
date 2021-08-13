package com.store.project.application.domain.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.store.project.application.domain.entity.Product;
import com.store.project.application.domain.entity.Stock;
import com.store.project.application.domain.entity.Store;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    private Long idx;

    private String pName;

    private String p_description;

    private int p_price;

    private String p_saveName;

    private String p_originalName;

    private int s_idx;

    private Stock stock;

    public void setStoreIdx(Product product){
        this.s_idx= Math.toIntExact(product.getStore().getS_idx());
    }

}
