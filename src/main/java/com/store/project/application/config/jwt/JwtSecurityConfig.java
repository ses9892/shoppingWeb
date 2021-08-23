package com.store.project.application.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//SecurityConfigurerAdapter 상속받고 여기서 필터를 처리후  JwtSecurityConfig를 시큐리티 설정에 추가한다.
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // JWT 필터를 시큐리티 UsernamePasswordAuthenticationFilter전에 부착
    @Override
    public void configure(HttpSecurity builder) throws Exception {
        JwtFilter filter =new JwtFilter(tokenProvider);
        builder.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

    }
}
