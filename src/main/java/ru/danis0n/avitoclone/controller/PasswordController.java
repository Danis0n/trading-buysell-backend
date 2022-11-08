package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.danis0n.avitoclone.service.password.PasswordService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/password/restore")
    public String restorePassword(@RequestParam("username") String username, @RequestParam("email") String email,
                                  HttpServletRequest request, HttpServletResponse response) {
        return passwordService.restorePassword(username, email,request,response);
    }

    @GetMapping("/password/restore/update")
    public RedirectView redirectPasswordUpdate(@RequestParam("token") String token, HttpServletRequest request) {
        return passwordService.redirectPasswordUpdate(token,request);
    }

    @PostMapping("/password/update")
    public String restoreUpdate(@RequestParam("token") String token, HttpServletRequest request) {
        return passwordService.restoreUpdate(token,request);
    }

}
