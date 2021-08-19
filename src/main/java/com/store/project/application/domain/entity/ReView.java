package com.store.project.application.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class ReView {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;


    @Column(nullable = false,length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    @Temporal(TemporalType.DATE)
    private Date date = new Date();

    @Column
    private String fileName;

    @Column
    private String originalFileName;

    @ManyToOne
    @JoinColumn(name = "product_p_idx")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_s_idx")
    private Store store;

    @OneToMany(mappedBy = "reView",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<ReViewComment> reViewComment = new ArrayList<>();


}
