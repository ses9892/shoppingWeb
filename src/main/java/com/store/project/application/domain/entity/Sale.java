package com.store.project.application.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
public class Sale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "category",nullable = false)
    private String category;

    @Temporal(TemporalType.DATE)
    private Date date = new Date();

    private int ea;

    private int price;

    @ManyToOne
    @JoinColumn(name = "product_p_idx")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_s_idx")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "client_userId")
    private Client client;

    @Column(nullable = false,length = 1)
    private String commit;

    @OneToOne(mappedBy = "sale",cascade = CascadeType.ALL)
    private Refund refund;



}
