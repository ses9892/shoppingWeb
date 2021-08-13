package com.store.project.application.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.store.project.application.domain.dto.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Client implements Serializable {

    @Id
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private String phone;

    @Column
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "client",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    private Store store;

    @OneToMany(mappedBy = "client",cascade = {CascadeType.REFRESH,CascadeType.MERGE})
    private List<Sale> sale = new ArrayList<>();
}
