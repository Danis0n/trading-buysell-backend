package ru.danis0n.avitoclone.service.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthService {
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void login(HttpServletRequest request, HttpServletResponse response);
    void auth(HttpServletRequest request, HttpServletResponse response);
}
