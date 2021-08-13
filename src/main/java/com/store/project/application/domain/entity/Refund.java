package com.store.project.application.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Refund {
    @Id
    private Long idx;

    @OneToOne
    @JoinColumn(name = "sale_idx")
    private Sale sale;

    @Temporal(TemporalType.DATE)
    private Date date = new Date();

    @Column(length = 100)
    private String reason;

    @ManyToOne
    @JoinColumn(name = "product_p_idx")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_s_idx")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "client_userId")
    private Client client;

    public Refund() {

    }
}
