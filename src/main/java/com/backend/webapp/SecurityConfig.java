package com.backend.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.backend.webapp.security.JwtTokenUtil;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.addFilterBefore(jwtTokenUtil, BasicAuthenticationFilter.class).csrf().disable().build(); // need to
                                                                                                             // add
                                                                                                             // cross
                                                                                                             // site
                                                                                                             // request
                                                                                                             // forgery
                                                                                                             // protection
    }
}
