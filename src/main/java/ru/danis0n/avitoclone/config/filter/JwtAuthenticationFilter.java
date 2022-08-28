package ru.danis0n.avitoclone.config.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.danis0n.avitoclone.dto.appuser.AppUserRequest;
import ru.danis0n.avitoclone.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // TODO : create parser for this...

        String jsonBody = request.getReader().lines().collect(Collectors.joining()).replace(" ", "");

        log.info("json is  " + jsonBody);

        Gson gson = new Gson();

        AppUserRequest appUserRequest = gson.fromJson(jsonBody,AppUserRequest.class);
        log.info(appUserRequest.username);
        log.info(appUserRequest.password);

        String username = appUserRequest.username;
        String password = appUserRequest.password;

        log.info("Username {} with password {}", username, password);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        Map<String,String> tokens = jwtUtil.generateTokenMap(user,algorithm,request);
        log.info("Tokens has been created for User {}", user.getUsername());

        response.setContentType(APPLICATION_JSON_VALUE);

        // TODO: UPGRADE THIS TO NEW CLASS OR SMTH...
        response.addCookie(new Cookie("refreshToken",tokens.get("refreshToken")));
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);

    }
}
