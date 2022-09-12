package ru.danis0n.avitoclone.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.dto.AuthResponse;
import ru.danis0n.avitoclone.dto.appuser.AppUser;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.service.refresh.RefreshTokenService;
import ru.danis0n.avitoclone.util.JwtUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final JwtUtil jwtUtil;
    private final ObjectMapperUtil mapperUtil;
    private final RefreshTokenService refreshTokenService;
    private final AppUserService appUserService;

    // TODO : REFACTOR IT !

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        try {
            String username = jwtUtil.getUsernameFromToken(authorizationHeader);
            AppUserEntity user = appUserService.getAppUserEntity(username);

            Map<String,String> tokens = jwtUtil.generateTokenMap(
                    user,
                    request
            );

            saveToken(user,tokens.get("refreshToken"));
            setResponseWithParams(response,tokens.get("refreshToken"),username);

            new ObjectMapper().writeValue(
                    response.getOutputStream(),
                    new AuthResponse(
                            tokens.get("accessToken"),
                            tokens.get("refreshToken"),
                            mapperUtil.mapToAppUserWithParams(user)
                    )
            );

        } catch (Exception e){
            manageException(e,response);
        }
    }

    @Override
    public void auth(HttpServletRequest request, HttpServletResponse response){

        try {
            Map<String,String> cookieMap = getCookieMapFromRequest(request.getCookies());

            String refreshToken = cookieMap.get("refreshToken");
            String username =  jwtUtil.getUsernameFromToken(refreshToken);

            AppUserEntity appUser = appUserService.getAppUserEntity(username);

            if(!refreshTokenService.validateToken(appUser,refreshToken)){
                // TODO : ...
                return;
            }

            Map<String,String> tokens = jwtUtil.generateTokenMap(appUser,request);
            refreshTokenService.saveToken(appUser,tokens.get("refreshToken"));

            setResponseWithParams(response,tokens.get("refreshToken"),username);

            new ObjectMapper().writeValue(
                    response.getOutputStream(),
                    new AuthResponse(
                            tokens.get("accessToken"),
                            tokens.get("refreshToken"),
                            mapperUtil.mapToAppUserWithParams(appUser)
                    )
            );

        }catch (Exception e){
            manageException(e,response);
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = jwtUtil.getUsernameFromRequest(request);
            refreshTokenService.deleteToken(
                    appUserService.getAppUserEntity(username)
            );
            setResponseDefault(response,username);

            try {
                new ObjectMapper().writeValue(response.getOutputStream(),"logout");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }catch (Exception e) {
            manageException(e,response);
        }
    }

    private void saveToken(AppUserEntity user, String token){
        refreshTokenService.saveToken(user,token);
    }

    private void setResponseWithParams(HttpServletResponse response, String token, String username){
        response.setContentType(APPLICATION_JSON_VALUE);
        response.addCookie(new Cookie("refreshToken", token));
        response.setHeader("username", username);
    }

    private void setResponseDefault(HttpServletResponse response, String username){
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("username",username);
    }

    private Map<String,String> getCookieMapFromRequest(Cookie[] cookies){
        Map<String,String> cookieMap = new HashMap<>();
        for(Cookie cookie : cookies){
            cookieMap.put(cookie.getName(),cookie.getValue());
        }
        return cookieMap;
    }

    private void manageException(Exception e,HttpServletResponse response){
        log.error("Error {}", e.getMessage());
        response.setHeader("error", e.getMessage());
        response.setStatus(FORBIDDEN.value());

        Map<String,String> error = new HashMap<>();
        error.put("error_message", e.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(),error);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
