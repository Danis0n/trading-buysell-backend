package ru.danis0n.avitoclone.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.danis0n.avitoclone.dto.AuthResponse;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.service.refresh.RefreshTokenService;
import ru.danis0n.avitoclone.util.JwtUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SuccessLogin implements ApplicationListener<AuthenticationSuccessEvent> {

    private final AppUserService appUserService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final ObjectMapperUtil mapperUtil;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent evt) {
        String username = evt.getAuthentication().getName();
        AppUserEntity user = appUserService.getAppUserEntityByUsername(username);

        Map<String,String> tokens = jwtUtil.generateTokenMap(user,request);
        saveToken(
                user,
                tokens.get("refreshToken")
        );

        setResponseWithParams(
                response,
                tokens.get("refreshToken"),
                username
        );

        try {
            new ObjectMapper().writeValue(response.getOutputStream(),
                    new AuthResponse(
                            tokens.get("accessToken"),
                            tokens.get("refreshToken"),
                            mapperUtil.mapToAppUserWithParams(user)
                            )
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setResponseWithParams(HttpServletResponse response, String token, String username){
        response.setContentType(APPLICATION_JSON_VALUE);
        response.addCookie(new Cookie("refreshToken", token));
        response.setHeader("username", username);
    }

    private void saveToken(AppUserEntity user, String token){
        refreshTokenService.saveToken(
                user,
                token
        );
    }

}