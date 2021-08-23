package com.store.project.application.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.project.application.response.ResponseData;
import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 401 auth 에러를 리턴하기위한 클래스
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String test =null;
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "계정 정보가 잘못됐거나, 로그인이 필요합니다.");
    }
}
