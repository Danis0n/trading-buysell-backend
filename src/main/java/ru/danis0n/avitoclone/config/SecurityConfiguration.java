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
import ru.danis0n.avitoclone.config.filter.JwtAuthenticationFilter;
import ru.danis0n.avitoclone.config.filter.JwtAuthorizationFilter;
import ru.danis0n.avitoclone.service.appuser.AppUserServiceImpl;
import ru.danis0n.avitoclone.util.JwtUtil;


import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), new JwtUtil());
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        http.authorizeRequests().
                antMatchers("/api/new-token").hasAnyAuthority("ROLE_NOT_CONFIRMED");

        http.authorizeRequests().
                antMatchers("/api/login","/api/token/refresh/**","/api/register/**").permitAll();

        http.authorizeRequests().
                antMatchers(GET,"/api/user/**").hasAnyAuthority("ROLE_USER");

        http.authorizeRequests().
                antMatchers(GET, "/api/users").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER");

        http.authorizeRequests().
                antMatchers(POST,"/api/role/add-role-to-user").hasAnyAuthority("ROLE_ADMIN","ROLE_SUPER_ADMIN");
        http.authorizeRequests().
                anyRequest().authenticated();

        http.authorizeRequests().
                and().
                formLogin().loginProcessingUrl("/api/").
                and().
                logout().logoutSuccessUrl("/api/");

        // think about it..
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

}