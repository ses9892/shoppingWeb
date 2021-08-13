package com.store.project.application.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@DynamicUpdate
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false,name = "pname")
    private String pName;

    private String p_description;

    @Column(nullable = false)
    private int p_price;

    private String p_saveName;

    private String p_originalName;

    @ManyToOne
    @JoinColumn(name = "store_s_idx")
    @JsonBackReference
    private Store store;

    @OneToOne(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonManagedReference
    private Stock stock;
}
