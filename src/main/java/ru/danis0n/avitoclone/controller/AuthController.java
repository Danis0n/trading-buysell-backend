package ru.danis0n.avitoclone.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.danis0n.avitoclone.service.auth.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request,response);
    }

    @PostMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        authService.login(request,response);
    }

    // TODO : сделать метод, который проверяет, залогинен ли юзверь. (путём проверки рефреш токена в базе)
    @PostMapping("/auth")
    public void auth(HttpServletRequest request, HttpServletResponse response) {
        authService.auth(request,response);
    }

}

