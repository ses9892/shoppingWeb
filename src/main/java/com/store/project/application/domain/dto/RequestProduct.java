package com.store.project.application.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestProduct {

    private int p_idx;

    private String pName;

    private String p_description;

    private int p_price;

    private String fileName; //original fileName

    private String fileBase64;

    private int st_ea;

    private Boolean st_flag;

}
