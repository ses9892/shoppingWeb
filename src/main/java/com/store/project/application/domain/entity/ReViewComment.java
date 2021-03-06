package com.store.project.application.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class ReViewComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 100)
    private String comment;

    @Temporal(TemporalType.DATE)
    private Date date = new Date();

    private String writer;

    @ManyToOne
    @JoinColumn(name = "ReView_idx")
    private ReView reView;
}
