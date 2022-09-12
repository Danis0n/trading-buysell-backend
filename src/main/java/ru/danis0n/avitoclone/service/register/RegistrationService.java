package ru.danis0n.avitoclone.service.register;

import ru.danis0n.avitoclone.dto.RegistrationRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpRequest;

public interface RegistrationService {
    String register(HttpServletRequest request, HttpServletResponse response);
    String confirmToken(String token);
    String updateToken(HttpServletRequest request);
}
