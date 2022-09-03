package ru.danis0n.avitoclone.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import ru.danis0n.avitoclone.config.filter.JwtAuthenticationFilter;
import ru.danis0n.avitoclone.config.filter.JwtAuthorizationFilter;
import ru.danis0n.avitoclone.util.JwtUtil;


import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)));
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");

        requestsConfigInit(http);

        http.addFilter(jwtAuthenticationFilter);
        http.addFilterBefore(new JwtAuthorizationFilter(new JwtUtil()), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public void requestsConfigInit(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        http.cors();

        String superAdmin = "ROLE_SUPER_ADMIN";
        String admin = "ROLE_ADMIN";
        String manager = "ROLE_MANAGER";
        String user = "ROLE_USER";
        String roleNotConfirmed = "ROLE_NOT_CONFIRMED";


        http.authorizeRequests().
                antMatchers("/api/login","/api/token/refresh/**","/api/register/**", "/api/auth").permitAll();

        http.authorizeRequests().
                antMatchers("/api/new/token").hasAnyAuthority(roleNotConfirmed);

        http.authorizeRequests().
                antMatchers(GET,"/api/advert/**").permitAll();
        http.authorizeRequests().
                antMatchers(POST,"/api/advert/create").hasAnyAuthority(superAdmin,admin,manager,user);

        http.authorizeRequests().
                antMatchers(GET,"/api/images/**").permitAll();

        http.authorizeRequests().
                antMatchers(POST,"/api/comment/**").hasAnyAuthority(superAdmin,admin,manager,user);
        http.authorizeRequests().
                antMatchers(DELETE,"/api/comment/**").hasAnyAuthority(superAdmin,admin,manager,user);
        http.authorizeRequests().
                antMatchers(GET,"/api/comment/**").hasAnyAuthority(superAdmin,admin,manager,user,roleNotConfirmed);

        http.authorizeRequests().
                antMatchers(GET, "/api/users/**").hasAnyAuthority(superAdmin,admin,manager,user);
        http.authorizeRequests().
                antMatchers(POST,"/api/users/**").hasAnyAuthority(superAdmin,admin);
        http.authorizeRequests().
                antMatchers(POST,"/api/role/**").hasAnyAuthority(superAdmin,admin);

        http.formLogin().defaultSuccessUrl("/api/login",true);
    }

//     TODO : MANAGE IT
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setMaxAge(1800L);
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}