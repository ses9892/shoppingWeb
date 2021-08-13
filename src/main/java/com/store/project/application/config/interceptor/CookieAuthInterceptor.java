package com.store.project.application.config.interceptor;

import com.store.project.application.Handling.exception.client.AnonyMousNotException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class CookieAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal.equals("anonymousUser")){
            throw new AnonyMousNotException("정상적인 요청이 아닙니다.");
        }
        return true;
    }
}
