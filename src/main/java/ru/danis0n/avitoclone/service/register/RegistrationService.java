package ru.danis0n.avitoclone.service.register;

import ru.danis0n.avitoclone.dto.appuser.RegistrationRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RegistrationService {
    String register(HttpServletRequest request, HttpServletResponse response, RegistrationRequest registrationRequest);
    String confirmToken(String token);
    String updateToken(HttpServletRequest request);
}
