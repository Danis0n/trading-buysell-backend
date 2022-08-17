package ru.danis0n.avitoclone.service.register.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.dto.Email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}") private String sender;

    @Override
    public String sendSimpleMail(Email email) {

        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(email.getRecipient());
            mailMessage.setText(email.getMsgBody());
            mailMessage.setSubject(email.getSubject());

            log.info("Email is sending.. to {} ", email.getRecipient());

            mailSender.send(mailMessage);
            return "Mail sent successfully..";
        } catch (Exception e) {
            return "Error while sending mail";
        }
    }

    @Override
    public String sendMailWithAttachment(Email email) {
        MimeMessage mimeMessage
                = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(email.getRecipient());
            mimeMessageHelper.setText(email.getMsgBody());
            mimeMessageHelper.setSubject(
                    email.getSubject());

            FileSystemResource file
                    = new FileSystemResource(
                    new File(email.getAttachment()));

            mimeMessageHelper.addAttachment(
                    Objects.requireNonNull(file.getFilename()), file);

            mailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (MessagingException e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }
}
