package ru.danis0n.avitoclone.service.register;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RegistrationService {
    String register(HttpServletRequest request, HttpServletResponse response);
    String confirmToken(String token);
    String updateToken(HttpServletRequest request);
}
