package ru.danis0n.avitoclone.service.register;

import org.springframework.web.servlet.view.RedirectView;
import ru.danis0n.avitoclone.dto.appuser.RegistrationRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RegistrationService {
    String register(HttpServletRequest request, HttpServletResponse response, RegistrationRequest registrationRequest);
    RedirectView confirmToken(String token);
    String updateToken(HttpServletRequest request);
}
