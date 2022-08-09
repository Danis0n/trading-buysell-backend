package ru.danis0n.avitoclone.service.register;

import ru.danis0n.avitoclone.dto.RegistrationRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

public interface RegistrationService {
    boolean isValidEmail(String email);
    boolean isValidUsername(String username);
    String register(RegistrationRequest request);
    String confirmToken(String token);
    String updateToken(HttpServletRequest request);
}
