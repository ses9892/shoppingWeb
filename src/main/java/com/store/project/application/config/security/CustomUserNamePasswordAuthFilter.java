package com.store.project.application.config.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CustomUserNamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    private Boolean postOnly = true;
    private HashMap<String,String> jsonRequest;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if(postOnly && !request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("Auth method not Supported : " + request.getMethod());
        }
        if(request.getHeader("Content-type").equals("application/json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.jsonRequest = objectMapper.readValue(request.getReader().lines().collect(Collectors.joining()),
                        new TypeReference<HashMap<String, String>>() {
                        });
            } catch (IOException e) {
                e.printStackTrace();
                throw new AuthenticationServiceException("Request Content-Type(application/json) Parsing Error");
            }
        }
            String username = obtainUsername(request);
            String password = obtainPassword(request);

            if(username==null){
                username = "";
            }
            if(password == null){
                password = "";
            }
            username.trim();

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
            setDetails(request,token);
            return this.getAuthenticationManager().authenticate(token);
        }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String pwdParameter = super.getPasswordParameter();
        if(request.getHeader("Content-type").equals("application/json")){
            return jsonRequest.get(pwdParameter);
        }
        return request.getParameter(pwdParameter);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String userIdParameter = super.getUsernameParameter();
        if(request.getHeader("Content-type").equals("application/json")){
            return jsonRequest.get(userIdParameter);
        }
        return request.getParameter(userIdParameter);
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        super.setPostOnly(postOnly);
    }
}
