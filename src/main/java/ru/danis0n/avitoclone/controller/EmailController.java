package ru.danis0n.avitoclone.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.danis0n.avitoclone.dto.Email;
import ru.danis0n.avitoclone.service.register.email.EmailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send/email")
    public String sendEmail(@RequestBody Email email){
        return emailService.sendSimpleMail(email);
    }

    @PostMapping("/send/email/attachment")
    public String sendEmailWithAttachment(@RequestBody Email email){
        return emailService.sendMailWithAttachment(email);
    }

}
