package ru.danis0n.avitoclone.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.dto.appuser.AppUser;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.util.JwtUtil;

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
    private final AppUserService userService;

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

//        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            log.info(request.getPathInfo());
            log.info(request.getQueryString());
            log.info(request.getHeaderNames().toString());

            try {
                String username = jwtUtil.getUsernameFromToken(authorizationHeader);

                log.info("Refresh with Username {}", username);
                AppUser user = userService.getAppUser(username);

                Map<String,String> tokens = jwtUtil.generateTokenMap(user,request);

                response.setContentType(APPLICATION_JSON_VALUE);
                response.addCookie(new Cookie("refreshToken", tokens.get("refreshToken")));
                response.setHeader("username",username);
//                response.setHeader("username",username);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);

            } catch (Exception e){
                log.error("Error {}", e.getMessage());
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String,String> error = new HashMap<>();
                error.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
//        } else{
//            throw new RuntimeException("Refresh token is missing");
//        }
    }
}
