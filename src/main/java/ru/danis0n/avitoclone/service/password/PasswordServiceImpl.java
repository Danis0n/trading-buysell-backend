package ru.danis0n.avitoclone.service.password;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import ru.danis0n.avitoclone.dto.Email;
import ru.danis0n.avitoclone.entity.token.PasswordToken;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.repository.password.PasswordTokenRepository;
import ru.danis0n.avitoclone.repository.user.AppUserRepository;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.service.register.email.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PasswordServiceImpl implements PasswordService{

    private final PasswordTokenRepository passwordTokenRepository;
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder encoder;
    private final AppUserService appUserService;
    private final EmailService emailService;

    @Override
    public String restorePassword(String username, String email) {
        AppUserEntity user = appUserService.getAppUserEntityByUsername(username);

        if(!user.getUserInfo().getEmail().equals(email)) return "Email is wrong!";

        String token = UUID.randomUUID().toString();
        PasswordToken passwordToken = new PasswordToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                user
        );

        String link = "http://localhost:8080/api/password/restore/update?token=" + token;

        emailService.sendSimpleMail(new Email(
                email,
                link,
                "Restore your password!",
                null
        ));

        passwordTokenRepository.save(passwordToken);
        return token;
    }

    @Override
    public String restoreUpdate(String token, String password) {
        PasswordToken passwordToken = passwordTokenRepository.findByToken(token).orElse(null);
        if(passwordToken == null)
            return "Token is unavailable!";

        LocalDateTime expiredAt = passwordToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now()))
            return "Token is expired";

        AppUserEntity user = passwordToken.getAppUser();

        user.setPassword(encoder.encode(password));
        appUserRepository.save(user);
        passwordToken.setAppUser(user);
        passwordTokenRepository.updateConfirmedAt(token,LocalDateTime.now());

        return "Success";
    }

    @Override
    public RedirectView redirectPasswordUpdate(String token) {
        RedirectView redirectView = new RedirectView();
        PasswordToken passwordToken = passwordTokenRepository.findByToken(token).orElse(null);

        if(passwordToken == null) {
            redirectView.setUrl("http://localhost:3000/password/update?token=" + token);
            return redirectView;
        }

        LocalDateTime expiredAt = passwordToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            redirectView.setUrl("http://localhost:3000/password/restore/expired");
            return redirectView;
        }

        redirectView.setUrl("http://localhost:3000/password/restore/unavailable");
        return redirectView;
    }


}
