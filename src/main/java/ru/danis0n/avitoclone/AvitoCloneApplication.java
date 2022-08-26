package ru.danis0n.avitoclone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import ru.danis0n.avitoclone.dto.AdvertType;
import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Email;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.RoleEntity;
import ru.danis0n.avitoclone.repository.RoleRepository;
import ru.danis0n.avitoclone.service.advert.AdvertService;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.service.appuser.AppUserServiceImpl;
import ru.danis0n.avitoclone.service.register.email.EmailService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class AvitoCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvitoCloneApplication.class, args);
    }

//    @Bean
    CommandLineRunner fill(AdvertService advertService, AppUserService appUserService){
        return args -> {
//            advertService.createType(new AdvertType(null,"AUTO"));
//            advertService.createType(new AdvertType(null,"JOB"));
//            advertService.createType(new AdvertType(null,"ANIMALS"));

//            appUserService.saveRole(new Role(null,"ROLE_BANNED"));
//            appUserService.saveRole(new Role(null,"ROLE_USER"));
//            appUserService.saveRole(new Role(null,"ROLE_ADMIN"));
//            appUserService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));
//            appUserService.saveRole(new Role(null,"ROLE_MANAGER"));
//
            appUserService.addRoleToAppUser("alex","ROLE_ADMIN");
            appUserService.addRoleToAppUser("alex","ROLE_SUPER_ADMIN");
            appUserService.addRoleToAppUser("alex","ROLE_MANAGER");


        };
    }

}