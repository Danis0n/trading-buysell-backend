package ru.danis0n.avitoclone.service.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthService {
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void auth(HttpServletRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
