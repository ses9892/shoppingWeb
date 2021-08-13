package com.store.project.application.Handling.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.project.application.Handling.exception.client.UserNotFoundException;
import com.store.project.application.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailedHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        String userId = request.getParameter("userId");
        ObjectMapper mapper = new ObjectMapper();	//JSON 변경용

        ResponseData responseData = null;
        if(e instanceof InternalAuthenticationServiceException){
            responseData = ResponseData.builder().status(UserNotFoundException.HTTP_STATUS)
                    .code(UserNotFoundException.code).message(e.getMessage()).build();
        }else if(e instanceof BadCredentialsException){
            responseData = ResponseData.builder().status(HttpStatus.BAD_REQUEST)
                    .code(UserNotFoundException.code).message("비밀번호가 맞지 않습니다.").build();
        }
        response.setStatus(400);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(mapper.writeValueAsString(responseData));
        response.getWriter().flush();
    }
}
