package com.backend.aula09.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/animals/register").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/adoptions/register").hasAnyRole("ADMIN", "USER")

                        .requestMatchers(HttpMethod.PUT, "/animals/up/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/adoptions/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers(HttpMethod.PATCH, "/animals/{param}/available").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/animals/{param}/location").hasAnyRole("ADMIN", "USER")

                        .requestMatchers(HttpMethod.GET, "/animals").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/adoptions").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/adoptions/with-details").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/animals/available").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
