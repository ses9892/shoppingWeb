package com.store.project.application.config.security;

import com.store.project.application.Handling.Handler.LoginFailedHandler;
import com.store.project.application.Handling.Handler.LoginSuccessHandler;
import com.store.project.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import sun.rmi.runtime.Log;

@Configuration
@EnableWebSecurity
@Slf4j
public class sercurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;


    //static 무시
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/**","/h2-console/**").permitAll()
        .anyRequest().authenticated()
        .and()
            .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll().and()
        .headers().frameOptions().disable();

        http.addFilterAt(getAuthFilter(), UsernamePasswordAuthenticationFilter.class);
//            .and().addFilterBefore(securityAuthenticationFilter(), FilterSecurityInterceptor.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    @Bean
    public securityAuthenticationFilter securityAuthenticationFilter(){
        securityAuthenticationFilter securityAuthenticationFilter = new securityAuthenticationFilter();
        return securityAuthenticationFilter;
    }
    @Bean
    protected CustomUserNamePasswordAuthFilter getAuthFilter(){
        CustomUserNamePasswordAuthFilter filter = new CustomUserNamePasswordAuthFilter();
        try {
            filter.setFilterProcessesUrl("/login");
            filter.setAuthenticationManager(this.authenticationManagerBean());
            filter.setUsernameParameter("userId");
            filter.setPasswordParameter("password");
            filter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
            filter.setAuthenticationFailureHandler(new LoginFailedHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }
}
