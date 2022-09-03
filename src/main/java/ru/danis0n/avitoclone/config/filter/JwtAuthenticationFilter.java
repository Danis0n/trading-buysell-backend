package ru.danis0n.avitoclone.config.filter;

import com.google.gson.Gson;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String jsonBody = getJson(request);
        LoginRequest loginRequest = new Gson().fromJson(jsonBody, LoginRequest.class);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return authenticationManager.authenticate(token);
    }

    private String getJson(HttpServletRequest request){
        try {
            return request.getReader().lines().collect(
                    Collectors.joining()).replace(" ", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

@Data
class LoginRequest {
    private String username;
    private String password;
}

