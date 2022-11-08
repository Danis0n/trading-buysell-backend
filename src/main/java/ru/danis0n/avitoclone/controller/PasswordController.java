package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.danis0n.avitoclone.service.password.PasswordService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/password/restore")
    public Boolean restorePassword(@RequestParam("username") String username, @RequestParam("email") String email) {
        return passwordService.restorePassword(username, email);
    }

    @GetMapping("/password/restore/update")
    public RedirectView redirectPasswordUpdate(@RequestParam("token") String token) {
        return passwordService.redirectPasswordUpdate(token);
    }

    @PostMapping("/password/update")
    public String restoreUpdate(@RequestParam("token") String token, @RequestParam("password") String password) {
        return passwordService.restoreUpdate(token,password);
    }

    @GetMapping("/password/restore/email/present")
    public Boolean isEmailPresent(@RequestParam("email") String email) {
        return passwordService.isEmailPresent(email);
    }

}
