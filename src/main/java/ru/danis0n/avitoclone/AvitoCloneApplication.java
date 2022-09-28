package ru.danis0n.avitoclone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

            String superAdmin = "ROLE_SUPER_ADMIN";
            String admin = "ROLE_ADMIN";
            String manager = "ROLE_MANAGER";
            String user = "ROLE_USER";
            String roleNotConfirmed = "ROLE_NOT_CONFIRMED";

            appUserService.addRoleToAppUser(appUserService.getAppUserEntityByUsername("alex"),superAdmin);
            appUserService.addRoleToAppUser(appUserService.getAppUserEntityByUsername("alex"),admin);
            appUserService.addRoleToAppUser(appUserService.getAppUserEntityByUsername("alex"),manager);
        };
    }

}