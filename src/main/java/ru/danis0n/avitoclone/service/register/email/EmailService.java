package ru.danis0n.avitoclone.service.register.email;

import ru.danis0n.avitoclone.dto.Email;

public interface EmailService {

    String sendSimpleMail(Email email);
    String sendMailWithAttachment(Email email);

}
