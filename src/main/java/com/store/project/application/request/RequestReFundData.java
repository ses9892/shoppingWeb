package com.store.project.application.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class RequestReFundData {

    //판매번호
    private int idx;

    private String reason;
}
