package com.store.project.application.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private String windowUploadImgesPath;
    private String linuxUploadImagesPath;
    private Environment env;


    @Autowired
    public WebMvcConfig(@Value("${custom.path.upload-images-window}") String windowUploadImgesPath,
                        @Value("${custom.path.upload-images-linux}")String linuxUploadImagesPath, Environment env) {
        this.windowUploadImgesPath=windowUploadImgesPath;
        this.linuxUploadImagesPath = linuxUploadImagesPath;
        this.env=env;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.equals("windows 10")){
            registry.addResourceHandler("/img/**")
                    .addResourceLocations(windowUploadImgesPath);
        }else{
            registry.addResourceHandler("/img/**")
                    .addResourceLocations(linuxUploadImagesPath);
        }
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8"); // 파일 인코딩 설정
        multipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024*100); // 파일당 업로드 크기 제한 (5MB)
        return multipartResolver;
    }

}
