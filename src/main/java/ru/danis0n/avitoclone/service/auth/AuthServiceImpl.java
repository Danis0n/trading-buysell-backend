package ru.danis0n.avitoclone.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.dto.AuthResponse;
import ru.danis0n.avitoclone.dto.appuser.AppUser;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
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

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final JwtUtil jwtUtil;
    private final ObjectMapperUtil mapperUtil;
    private final RefreshTokenService refreshTokenService;
    private final AppUserService appUserService;

    @Override
    public void auth(HttpServletRequest request, HttpServletResponse response){

        try {
            Map<String,String> cookieMap = getCookieMapFromRequest(request.getCookies());
            String refreshToken = cookieMap.get("refreshToken");

            AppUserEntity appUser = getAppUserEntityByUsername(
                    getUsernameFromToken(refreshToken));

            if(!validateToken(appUser, refreshToken) || !validateTime(refreshToken) ){
                deleteToken(appUser);
                manageOutOfDate(response);
                return;
            }

            Map<String,String> tokens = generateTokenMap(
                    appUser,
                    request
            );

            saveToken(appUser,tokens.get("refreshToken"));
            setResponseWithParams(response,tokens.get("refreshToken"), appUser.getUsername());

            new ObjectMapper().writeValue(
                    response.getOutputStream(),
                    new AuthResponse(
                            tokens.get("accessToken"),
                            tokens.get("refreshToken"),
                            mapToAppUserWithParams(appUser)
                    )
            );

        }catch (Exception e){
            manageException(e,response);
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        try {
            String username = getUsernameFromRequest(request);
            deleteToken(getAppUserEntityByUsername(username));
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

    private void deleteToken(AppUserEntity user){
        refreshTokenService.deleteToken(user);
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
        response.setStatus(UNAUTHORIZED.value());

        Map<String,String> error = new HashMap<>();
        error.put("error_message", e.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(),error);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void manageOutOfDate(HttpServletResponse response){
        response.setStatus(UNAUTHORIZED.value());
        Map<String,String> error = new HashMap<>();
        error.put("value", "out of date");
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(),error);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getUsernameFromRequest(HttpServletRequest request){
        return jwtUtil.getUsernameFromRequest(request);
    }

    private String getUsernameFromToken(String refreshToken){
        return jwtUtil.getUsernameFromToken(refreshToken);
    }

    private Map<String,String> generateTokenMap(AppUserEntity user, HttpServletRequest request){
        return jwtUtil.generateTokenMap(user,request);
    }

    private AppUser mapToAppUserWithParams(AppUserEntity user){
        return mapperUtil.mapToAppUserWithParams(user);
    }

    private AppUserEntity getAppUserEntityByUsername(String username){
        return appUserService.getAppUserEntityByUsername(username);
    }

    private boolean validateToken(AppUserEntity user, String refreshToken){
        return refreshTokenService.validateToken(user,refreshToken);
    }

    private boolean validateTime(String token){
        return jwtUtil.validateTime(token);
    }

}
