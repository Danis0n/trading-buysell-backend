package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ru.danis0n.avitoclone.dto.appuser.RegistrationRequest;
import ru.danis0n.avitoclone.service.register.RegistrationService;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "file", required = false) MultipartFile file,
                        @RequestParam("name") String name,
                        @RequestParam("password") String password,
                        @RequestParam("username") String username,
                        @RequestParam("email") String email,
                        @RequestParam("phone") String phone) {
        return registrationService.register(request,response, new RegistrationRequest(name,username,password,email,phone,file));
    }

    @GetMapping("/register/confirm")
    public RedirectView confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }

    @GetMapping("/new/token")
    public String update(HttpServletRequest request){
        return registrationService.updateToken(request);
    }

}
