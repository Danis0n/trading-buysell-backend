package ru.danis0n.avitoclone.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.appuser.AppUser;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.RoleEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class JwtUtil implements Serializable {

    // TODO : FIX token live-time

    public DecodedJWT getDecodedJwt(Algorithm algorithm, String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private Algorithm getAlgorithm(String secret){
        return Algorithm.HMAC256(secret.getBytes());
    }

    public Map<String, String> generateTokenMap(AppUserEntity user, HttpServletRequest request) {
        Algorithm algorithm = getAlgorithm("secret");
        Map<String,String> tokens = new HashMap<>();
        tokens.put("accessToken",generateAccessToken(user,algorithm,request));
        tokens.put("refreshToken",generateRefreshToken(user.getUsername(),algorithm,request));
        return tokens;
    }

    private String generateAccessToken(AppUserEntity user, Algorithm algorithm, HttpServletRequest request){
        return  JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis() + 20 * 30 * 60 * 1000)).
                withIssuer(request.getRequestURI()).
                withClaim("roles",user.getRoles().
                        stream().map(RoleEntity::getName).collect(Collectors.toList())).
                sign(algorithm);
    }

    private String generateRefreshToken(String username, Algorithm algorithm, HttpServletRequest request){
        return  JWT.create().
                withSubject(username).
                withExpiresAt(new Date(System.currentTimeMillis() + 30 * 30 * 60 * 1000)).
                withIssuer(request.getRequestURI()).
                sign(algorithm);
    }

    public String getUsernameFromRequest(HttpServletRequest request){
        String tokenFromRequest = request.getHeader(AUTHORIZATION);
        if(!tokenFromRequest.startsWith("Bearer "))
            return null;
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
    // TODO : check it
    public Boolean validateTime(String token){
        DecodedJWT decodedJWT = getDecodedJwt("secret",token);
        return decodedJWT.getExpiresAt().getTime()
                >= new Date(System.currentTimeMillis()).getTime();
    }

    public Collection<SimpleGrantedAuthority> getAuthorities(String[] roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach( role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }
}
