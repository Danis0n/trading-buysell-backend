package ru.danis0n.avitoclone.service.password;

import org.springframework.web.servlet.view.RedirectView;

public interface PasswordService {
    Boolean restorePassword(String username, String email);
    String restoreUpdate(String token, String password);
    RedirectView redirectPasswordUpdate(String token);
    Boolean isEmailPresent(String email);
}
