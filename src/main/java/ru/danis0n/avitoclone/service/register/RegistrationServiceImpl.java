package ru.danis0n.avitoclone.service.register;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.dto.Email;
import ru.danis0n.avitoclone.dto.RegistrationRequest;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.ConfirmationToken;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.service.confirm.ConfirmationTokenService;
import ru.danis0n.avitoclone.service.register.email.EmailService;
import ru.danis0n.avitoclone.util.JsonUtil;
import ru.danis0n.avitoclone.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService{

    private final AppUserService appUserService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final JsonUtil jsonUtil;
    private final ConfirmationTokenService confirmationTokenService;

    @Value("${spring.config.registration.confirm.link}")
    private String confirmLink;

    @Value("${spring.config.registration.not.valid.email}")
    private String emailIsNotValid;

    @Value("${spring.config.registration.mail.token.already.confirmed}")
    private String emailAlreadyConfirmed;

    @Value("${spring.config.registration.not.valid.username}")
    private String usernameIsNotValid;

    @Value("${spring.config.registration.not.valid.token}")
    private String tokenNotFound;

    @Value("${spring.config.registration.mail.token.expired}")
    private String tokenExpired;

    @Value("${spring.config.registration.mail.token.confirmed}")
    private String tokenConfirmed;

    @Override
    public boolean isValidEmail(String email) {
        return appUserService.isExistsAppUserEntityByEmail(email);
    }

    @Override
    public boolean isValidUsername(String username) {
        return appUserService.isExistsAppUserEntityByUsername(username);
    }

    @Override
    public String register(HttpServletRequest request, HttpServletResponse response) {

        RegistrationRequest registrationRequest = new Gson().fromJson(
                jsonUtil.getJson(request),
                RegistrationRequest.class
        );

        if(isValidEmail(registrationRequest.getEmail())){
            return(emailIsNotValid);
        }
        if(isValidUsername(registrationRequest.getUsername())){
            return(usernameIsNotValid);
        }

        String token = appUserService.saveAppUser(registrationRequest);

        String link = confirmLink + token;

        emailService.sendSimpleMail(new Email(
                registrationRequest.getEmail(),
                link,
                "Hey!",
                null
        ));

        return token;
    }

    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.
                getToken(token).orElse(null);

        if(confirmationToken == null){
            return tokenNotFound;
        }

        if(confirmationToken.getConfirmedAt() != null){
            return(emailAlreadyConfirmed);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            return(tokenExpired);
        }

        confirmationTokenService.setConfirmedAt(token);
        String username = confirmationToken.getAppUser().getUsername();

        appUserService.enableAppUser(username);
        appUserService.removeRoleFromAppUser(username,"ROLE_NOT_CONFIRMED");
        appUserService.addRoleToAppUser(appUserService.getAppUserEntity(username),"ROLE_USER");

        return tokenConfirmed;
    }

    @Override
    public String updateToken(HttpServletRequest request) {

        String tokenFromRequest = request.getHeader(AUTHORIZATION);

        if(tokenFromRequest != null && tokenFromRequest.startsWith("Bearer ")){

            String username = jwtUtil.getUsernameFromToken(tokenFromRequest);
            AppUserEntity user = appUserService.getAppUserEntity(username);
            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1),
                    user
            );
            confirmationTokenService.saveConfirmationToken(confirmationToken);

            return token;
        }
        else return tokenFromRequest;
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}