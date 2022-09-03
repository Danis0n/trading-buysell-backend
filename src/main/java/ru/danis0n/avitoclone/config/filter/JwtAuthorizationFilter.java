package ru.danis0n.avitoclone.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.danis0n.avitoclone.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")){
            filterChain.doFilter(request,response);
        } else {

            String tokenFromRequest = request.getHeader(AUTHORIZATION);
            if(tokenFromRequest != null && tokenFromRequest.startsWith("Bearer ")) {
                try {

                    String token = tokenFromRequest.substring("Bearer ".length());
                    String username = jwtUtil.getUsernameFromToken(token);

                    Collection<SimpleGrantedAuthority> authorities = jwtUtil.getAuthorities(
                            jwtUtil.getRolesFromToken(token));

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request,response);

                } catch (Exception e){
                    log.error("Error logging in {}", e.getMessage());
                    response.setHeader("error",e.getMessage());
                    response.setStatus(FORBIDDEN.value());

                    Map<String,String> error = new HashMap<>();
                    error.put("error_message",e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                }
            } else{
                filterChain.doFilter(request,response);
            }
        }

    }
}
