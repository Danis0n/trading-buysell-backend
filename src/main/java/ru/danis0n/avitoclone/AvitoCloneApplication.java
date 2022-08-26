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
//            advertService.createType(new AdvertType(null,"AUTO"));
//            advertService.createType(new AdvertType(null,"JOB"));
//            advertService.createType(new AdvertType(null,"ANIMALS"));

//            appUserService.saveRole(new Role(null,"ROLE_BANNED"));
//            appUserService.saveRole(new Role(null,"ROLE_USER"));
//            appUserService.saveRole(new Role(null,"ROLE_ADMIN"));
//            appUserService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));
//            appUserService.saveRole(new Role(null,"ROLE_MANAGER"));
//
//            appUserService.addRoleToAppUser("alex","ROLE_ADMIN");
//            appUserService.addRoleToAppUser("alex","ROLE_SUPER_ADMIN");
//            appUserService.addRoleToAppUser("alex","ROLE_MANAGER");


        };
    }

}