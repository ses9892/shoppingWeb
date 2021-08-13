package com.store.project.application.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class securityAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
                                    throws ServletException, IOException {
        Boolean flag =true;
        HttpSession session = request.getSession();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal.equals("anonymousUser")){
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
        }
        filterChain.doFilter(request,response);
    }
}
