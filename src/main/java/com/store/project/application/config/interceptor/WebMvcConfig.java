package com.store.project.application.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CookieAuthInterceptor cookieAuthInterceptor;

    @Autowired
    public WebMvcConfig(CookieAuthInterceptor cookieAuthInterceptor) {
        this.cookieAuthInterceptor = cookieAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(cookieAuthInterceptor)
//                .addPathPatterns("/api/v1/**")
//                .excludePathPatterns("/api/v1/login.do","/api/v1/register.do","/api/v1/duplication.do");
        //  user/login = 로그인 요청을 받으면 요청메소드를 처리전에 인터셉터에서 걸어둔 필터를 한번 거친다.
    }
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8"); // 파일 인코딩 설정
        multipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024*100); // 파일당 업로드 크기 제한 (5MB)
        return multipartResolver;
    }

}
