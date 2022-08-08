package ru.danis0n.avitoclone.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.RoleEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String, String> generateTokenMap(AppUser user, Algorithm algorithm, HttpServletRequest request) {
        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token",generateAccessToken(user,algorithm,request));
        tokens.put("refresh_token",generateRefreshToken(user.getUsername(),algorithm,request));
        return tokens;
    }

    private String generateAccessToken(User user, Algorithm algorithm, HttpServletRequest request){
        return  JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)).
                withIssuer(request.getRequestURI()).
                withClaim("roles",user.getAuthorities().
                        stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).
                sign(algorithm);
    }

    private String generateAccessToken(AppUser user, Algorithm algorithm, HttpServletRequest request){
        return  JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)).
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

}
