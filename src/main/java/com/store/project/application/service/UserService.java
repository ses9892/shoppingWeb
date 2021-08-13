package com.store.project.application.service;

import com.store.project.application.domain.dto.ClientDto;
import com.store.project.application.request.RequestClientInfoChange;
import com.store.project.application.response.ResponseData;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    ResponseData register(ClientDto clientDto);

    ResponseData duplication(ClientDto clientDto);

    ResponseData myinfo();

    ResponseData infoChange(RequestClientInfoChange info);

    ResponseData infoDelete();
}
