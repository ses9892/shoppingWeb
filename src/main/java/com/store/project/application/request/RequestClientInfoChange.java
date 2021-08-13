package com.store.project.application.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestClientInfoChange {

    private String password;

    private String name;

    private String phone;

    private String address;

    private String email;

}
