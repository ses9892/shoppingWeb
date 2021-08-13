package com.store.project.application.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.store.project.application.domain.entity.Client;
import com.store.project.application.domain.entity.Sale;
import com.store.project.application.domain.entity.Store;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {

    @Size(min = 4,max = 12)
    private String userId;
    @Size(min = 4,max = 12)
    private String password;

    @Size(min = 3,max = 4)
    private String name;

    private String phone;

    private String address;

    private Role role;

    private int s_idx;

    private List<Integer> sale_idx_list ;

    public void setSaleList(Client client){
        sale_idx_list = new ArrayList<>();
        List<Sale> sale = client.getSale();
        for (Sale s:sale) {
            if(s.getCommit().equals("1")){
            sale_idx_list.add(Math.toIntExact(s.getIdx()));
            }
        }
    }
}
