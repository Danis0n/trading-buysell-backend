package ru.danis0n.avitoclone.service.auth;

import ru.danis0n.avitoclone.dto.AuthResponse;
import ru.danis0n.avitoclone.dto.appuser.AppUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthService {
    void auth(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void logout(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
