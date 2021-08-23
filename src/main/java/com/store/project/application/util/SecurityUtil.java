package com.store.project.application.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class SecurityUtil {


    public static Optional<String> getCurrentUserName(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication==null){
            log.debug("Security Context 인증정보가 없습니다");
            return Optional.empty();
        }
        String userId = null;
        if(authentication.getPrincipal() instanceof UserDetails){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userId = userDetails.getUsername();
        }else if(authentication.getPrincipal() instanceof  String){
            userId = (String) authentication.getPrincipal();
        }
        return Optional.ofNullable(userId);
    }
}
