package ru.danis0n.avitoclone.service.password;

import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PasswordService {
    String restorePassword(String username, String email, HttpServletRequest request, HttpServletResponse response);
    String restoreUpdate(String token, String password);
    RedirectView redirectPasswordUpdate(String token, HttpServletRequest request);
}
