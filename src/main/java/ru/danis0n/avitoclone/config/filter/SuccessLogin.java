package ru.danis0n.avitoclone.config.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.service.refresh.RefreshTokenService;
import ru.danis0n.avitoclone.util.JwtUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
@Slf4j
@RequiredArgsConstructor
public class SuccessLogin implements ApplicationListener<AuthenticationSuccessEvent> {

    private final AppUserService appUserService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    // TODO : TO REFACTOR THIS
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent evt) {
        String username = evt.getAuthentication().getName();

        AppUserEntity appUser = appUserService.getAppUserEntity(username);

        User user = (User) evt.getAuthentication().getPrincipal();

        Map<String,String> tokens = jwtUtil.generateTokenMap(user,request);
        log.info("Tokens has been created for User {}", user.getUsername());
        response.setContentType(APPLICATION_JSON_VALUE);

        refreshTokenService.saveToken(appUser,tokens.get("refreshToken"));

//         TODO: UPGRADE THIS TO NEW CLASS OR SMTH...
        response.addCookie(new Cookie("refreshToken",tokens.get("refreshToken")));
        try {
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//         TODO : CHECK THE REQUEST BODY !!!!

    }
}
