package com.store.project.application.Handling.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.project.application.config.jwt.TokenProvider;
import com.store.project.application.response.ResponseData;
import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private TokenProvider tokenProvider;

    public LoginSuccessHandler(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> items = new HashMap<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map map2 = mapper.convertValue(principal, Map.class);
        LinkedHashMap<String,String> attributes = (LinkedHashMap<String, String>) map2.get("attributes");
        if(attributes !=null){
            // Oauth2
            String user_role = authentication.getAuthorities().iterator().next().toString();
            items.put("userId",attributes.get("email"));
            items.put("role",user_role);
        }else{
            // normal
            UserDetails userDetails = (UserDetails) principal;
            items.put("userId",userDetails.getUsername());
            items.put("role",userDetails.getAuthorities());
        }
        String token = tokenProvider.createToken(authentication);
        items.put("access_token",token);
        //토큰 response
        ResponseData responseData = ResponseData.builder()
                                    .status(HttpStatus.OK).code(ResponseDataStatus.SUCCESS)
                                    .message("Login Success!")
                                    .item(items)
                                    .build();
//        response.setHeader("AccessToken",token);
        response.getWriter().print(mapper.writeValueAsString(responseData));
        response.getWriter().flush();
        //JWT 쓸건지 안쓸건지의 유무
    }
}
