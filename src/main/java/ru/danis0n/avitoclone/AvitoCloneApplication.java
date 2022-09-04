package ru.danis0n.avitoclone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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

//            appUserService.saveRole(new Role("ROLE_NOT_CONFIRMED"));
//            appUserService.saveRole(new Role(null,"ROLE_MANAGER"));
//
//            appUserService.addRoleToAppUser("alex","ROLE_ADMIN");
//            appUserService.addRoleToAppUser("alex","ROLE_SUPER_ADMIN");
//            appUserService.addRoleToAppUser("alex","ROLE_MANAGER");

            appUserService.addRoleToAppUser(appUserService.getAppUserEntity("dasa"),"ROLE_NOT_CONFIRMED");


        };
    }

}