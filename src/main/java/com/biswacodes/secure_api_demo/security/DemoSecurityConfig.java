package com.biswacodes.secure_api_demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {
    
    // add support for JDBC ... no hardcoded users 

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        return new JdbcUserDetailsManager(dataSource);
    }


    // assign roles 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer -> 
            configurer
                     .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                     .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE")
                     .requestMatchers(HttpMethod.GET,"/api/employees/**").hasRole("EMPLOYEE")
                     .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
                     .requestMatchers(HttpMethod.PUT, "/api/employees").hasRole("MANAGER")
                     .requestMatchers(HttpMethod.PATCH, "/api/employees/**").hasRole("MANAGER")
                     .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
        );

        // use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery (CSRF)
        http.csrf(csrf -> csrf.disable());


        return http.build();
    }
}
