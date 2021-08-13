package com.store.project.application.Handling.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.project.application.response.ResponseData;
import com.store.project.application.response.ResponseDataStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        HashMap<String,Object> items = new HashMap<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        items.put("userId",userDetails.getUsername());
        items.put("role",userDetails.getAuthorities());
        //토큰 response
        ResponseData responseData = ResponseData.builder()
                                    .status(HttpStatus.OK).code(ResponseDataStatus.SUCCESS)
                                    .message("Login Success!")
                                    .item(items)
                                    .build();
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().print(mapper.writeValueAsString(responseData));
        response.getWriter().flush();
        //JWT 쓸건지 안쓸건지의 유무
    }
}
