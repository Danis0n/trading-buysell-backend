package ru.danis0n.avitoclone.service.register.email;

public interface EmailSender {
    void send(String email, String buildEmail);
}
