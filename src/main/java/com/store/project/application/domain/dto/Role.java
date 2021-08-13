package com.store.project.application.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    //스프링 시큐리티에서 권한에 사용되는 클래스 , 무조건 ROLE을 붙여야함

    GUEST("ROLE_ADMIN", "운영자"),
    SELLER("ROLE_SELLER","판매자"),
    USER("ROLE_USER", "일반 사용자");

    public final String key;
    public final String title;
}
