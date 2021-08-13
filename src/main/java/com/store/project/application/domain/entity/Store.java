package com.store.project.application.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
public class Store implements Serializable {

    @Id
    private Long s_idx;

    private String stName;

    private String stDelivery;

    private String stBrand;

    @OneToOne
    @JoinColumn(name = "client_userId")
    @JsonBackReference
    private Client client;

    @OneToMany(mappedBy = "store",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> product = new ArrayList<>();

}
/*
insert into store(s_idx,s_brand,user_id,client_user_id,s_delivery,s_name) values(970617,'jinho','ses9892','ses9892','true','장진호사업장');
 */
