package com.store.project.application.config.security;

import com.store.project.application.Handling.Handler.LoginFailedHandler;
import com.store.project.application.Handling.Handler.LoginSuccessHandler;
import com.store.project.application.config.jwt.JwtAccessDeniedHandler;
import com.store.project.application.config.jwt.JwtAuthenticationEntryPoint;
import com.store.project.application.config.jwt.JwtSecurityConfig;
import com.store.project.application.config.jwt.TokenProvider;
import com.store.project.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class sercurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    UserService userService;

    @Autowired
    public sercurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler
    ,UserService userService) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.userService = userService;
    }


    //static 무시
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);
        http.authorizeRequests()
                .antMatchers("/api/v1/register.do").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/v1/duplication.do").permitAll()
                .antMatchers("/api/v1/store/list","/api/v1/store/list/search").permitAll()
                .antMatchers("/api/v1/product/list").permitAll()
                .antMatchers("/api/v1/product/search").permitAll()
                //상품조회는 인증없이통과
                .antMatchers(HttpMethod.GET,"/api/v1/product").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/product/sale").permitAll()
                .antMatchers("/api/v1/review/list","/api/v1/review/search").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/review").permitAll()
        .anyRequest().authenticated()
        .and()
            .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();
        // 401 , 403 Token Exception Handling
        http.headers().frameOptions().sameOrigin();//h2-console
        //세션미사용
        http.addFilterAt(getAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.apply(new JwtSecurityConfig(tokenProvider));
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
            filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(tokenProvider));
            filter.setAuthenticationFailureHandler(new LoginFailedHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
