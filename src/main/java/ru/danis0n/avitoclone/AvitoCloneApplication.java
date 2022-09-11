package ru.danis0n.avitoclone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.danis0n.avitoclone.dto.AdvertType;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.service.advert.AdvertService;
import ru.danis0n.avitoclone.service.appuser.AppUserService;

@SpringBootApplication
public class AvitoCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvitoCloneApplication.class, args);
    }

//    @Bean
    CommandLineRunner fill(AdvertService advertService, AppUserService appUserService){
        return args -> {

            advertService.createType(new AdvertType("job"));
            advertService.createType(new AdvertType("animal"));
            advertService.createType(new AdvertType("auto"));

            String superAdmin = "ROLE_SUPER_ADMIN";
            String admin = "ROLE_ADMIN";
            String manager = "ROLE_MANAGER";
            String user = "ROLE_USER";
            String roleNotConfirmed = "ROLE_NOT_CONFIRMED";

            appUserService.saveRole(new Role(superAdmin));
            appUserService.saveRole(new Role(admin));
            appUserService.saveRole(new Role(manager));
            appUserService.saveRole(new Role(user));
            appUserService.saveRole(new Role(roleNotConfirmed));
            appUserService.saveRole(new Role("ROLE_BANNED"));

        };
    }

}