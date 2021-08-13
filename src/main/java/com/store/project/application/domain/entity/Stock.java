package com.store.project.application.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

//재고
@Entity
@Data
@DynamicUpdate
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long st_idx;

    @OneToOne
    @JoinColumn(name = "store_s_idx")
    @JsonBackReference
    private Product product;

    private Boolean st_flag;

    private int st_ea;


}
