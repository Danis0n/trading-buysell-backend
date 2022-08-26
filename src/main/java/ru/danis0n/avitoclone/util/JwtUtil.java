package ru.danis0n.avitoclone.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Role;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class JwtUtil implements Serializable {

    public DecodedJWT getDecodedJwt(Algorithm algorithm, String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public Map<String, String> generateTokenMap(User user, Algorithm algorithm, HttpServletRequest request) {
        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token",generateAccessToken(user,algorithm,request));
        tokens.put("refresh_token",generateRefreshToken(user.getUsername(),algorithm,request));
        return tokens;
    }

    public Map<String, String> generateTokenMap(AppUser user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token",generateAccessToken(user,algorithm,request));
        tokens.put("refresh_token",generateRefreshToken(user.getUsername(),algorithm,request));
        return tokens;
    }

    private String generateAccessToken(User user, Algorithm algorithm, HttpServletRequest request){
        return  JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000)).
                withIssuer(request.getRequestURI()).
                withClaim("roles",user.getAuthorities().
                        stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).
                sign(algorithm);
    }

    private String generateAccessToken(AppUser user, Algorithm algorithm, HttpServletRequest request){
        return  JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000)).
                withIssuer(request.getRequestURI()).
                withClaim("roles",user.getRoles().
                        stream().map(Role::getName).collect(Collectors.toList())).
                sign(algorithm);
    }

    private String generateRefreshToken(String username, Algorithm algorithm, HttpServletRequest request){
        return  JWT.create().
                withSubject(username).
                withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)).
                withIssuer(request.getRequestURI()).
                sign(algorithm);
    }

    public String getUsernameFromRequest(HttpServletRequest request){
        String tokenFromRequest = request.getHeader(AUTHORIZATION);
        String token = tokenFromRequest.substring("Bearer ".length());
        return getUsernameFromToken(token);
    }

    public String getUsernameFromToken(String token){
        DecodedJWT decodedJWT = getDecodedJwt("secret",token);
        return decodedJWT.getSubject();
    }

    public String[] getRolesFromToken(String token){
        DecodedJWT decodedJWT = getDecodedJwt("secret",token);
        return decodedJWT.getClaim("roles").asArray(String.class);
    }

    public DecodedJWT getDecodedJwt(String secret,String token){
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return getDecodedJwt(algorithm,token);
    }

    public Collection<SimpleGrantedAuthority> getAuthorities(String[] roles, String username) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach( role -> {
            log.info("{} - {}",username, role);
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }
}
