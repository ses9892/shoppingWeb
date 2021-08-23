package com.store.project.application.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


// GenericFilterBean 상속받음으로써 security 필터 사이에 넣을수 있는 doFilter 를 재정의해 구현 할 수 있다.
@Slf4j
public class JwtFilter extends GenericFilterBean {


    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //Token의 "Bearer" 제거 작업
        String jwt = this.resolveToken(httpServletRequest);
        //요청받은 URI
        String requestURI = httpServletRequest.getRequestURI();
        //jwt가 있나 확인 + 유효성 확인
        if(StringUtils.hasText(jwt)&& tokenProvider.validToken(jwt)){
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            // security principal에 저장한 auth 를 get 후 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에"+ authentication.getName()+ "인증 정보를 저장했습니다, uri: "+requestURI);
        } else {
            log.info("유효한 JWT 토큰이 없습니다, uri: "+requestURI);
        }
        //다음필터로 이동
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}