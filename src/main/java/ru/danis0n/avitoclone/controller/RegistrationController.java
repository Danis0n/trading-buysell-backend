package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.danis0n.avitoclone.service.register.RegistrationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response){
        return registrationService.register(request,response);
    }

    @GetMapping("/register/confirm")
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }

    @GetMapping("/new/token")
    public String update(HttpServletRequest request){
        return registrationService.updateToken(request);
    }

}
