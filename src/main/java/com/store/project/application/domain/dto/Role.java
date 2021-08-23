package com.store.project.application.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    //스프링 시큐리티에서 권한에 사용되는 클래스 , 무조건 ROLE을 붙여야함

    ADMIN("ROLE_ADMIN"),
    SELLER("ROLE_SELLER"),
    USER("ROLE_USER");

    public final String key;
}
