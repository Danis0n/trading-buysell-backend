package ru.danis0n.avitoclone.service.register.email;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.danis0n.avitoclone.dto.Email;

@SpringBootTest
class EmailServiceImplTest {

    private final EmailService emailService;

    EmailServiceImplTest(EmailService emailService) {
        this.emailService = emailService;
    }

    @Test
    public void sendEmail(){
    }

}