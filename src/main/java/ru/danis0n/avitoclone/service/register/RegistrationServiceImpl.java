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

    // TODO : REFACTOR IT !

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

        String token = saveAppUserWithToken(registrationRequest);

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
        ConfirmationToken confirmationToken = getToken(token);

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

        setConfirmedAt(token);
        String username = confirmationToken.getAppUser().getUsername();

        AppUserEntity appUser = getAppUserByUsername(username);

        enableAppUser(username);
        removeRoleFromAppUser(appUser,"ROLE_NOT_CONFIRMED");
        addRoleToAppUser(appUser,"ROLE_USER");

        return tokenConfirmed;
    }

    @Override
    public String updateToken(HttpServletRequest request) {

        String tokenFromRequest = request.getHeader(AUTHORIZATION);
    
        if(tokenFromRequest != null && tokenFromRequest.startsWith("Bearer ")){

            String username = getUsernameFromToken(tokenFromRequest);
            AppUserEntity user = getAppUserByUsername(username);
            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1),
                    user
            );
            saveToken(confirmationToken);
            return token;
        }
        else return tokenFromRequest;
    }

    private void enableAppUser(String username){
        appUserService.enableAppUser(username);
    }

    private void removeRoleFromAppUser(AppUserEntity user, String role){
        appUserService.removeRoleFromAppUser(user,role);
    }

    private void addRoleToAppUser(AppUserEntity user, String role){
        appUserService.addRoleToAppUser(user,role);
    }

    private boolean isValidEmail(String email) {
        return appUserService.isExistsAppUserEntityByEmail(email);
    }

    private boolean isValidUsername(String username) {
        return appUserService.isExistsAppUserEntityByUsername(username);
    }

    private AppUserEntity getAppUserByUsername(String username){
        return appUserService.getAppUserEntityByUsername(username);
    }

    private String saveAppUserWithToken(RegistrationRequest request){
        return appUserService.saveAppUser(request);
    }

    private String getUsernameFromToken(String token){
        return jwtUtil.getUsernameFromToken(token);
    }

    private ConfirmationToken getToken(String token){
        return confirmationTokenService.getToken(token).orElse(null);
    }

    private void setConfirmedAt(String token){
        confirmationTokenService.setConfirmedAt(token);
    }

    private void saveToken(ConfirmationToken token){
        confirmationTokenService.saveConfirmationToken(token);
    }

}