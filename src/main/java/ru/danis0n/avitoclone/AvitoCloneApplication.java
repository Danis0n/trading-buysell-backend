package ru.danis0n.avitoclone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.RoleEntity;
import ru.danis0n.avitoclone.service.appuser.AppUserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class AvitoCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvitoCloneApplication.class, args);
    }

//    @Bean
    CommandLineRunner run(AppUserServiceImpl userService){
        return args -> {

            userService.saveRole(new Role(null,"ROLE_NOT_CONFIRMED"));

//            userService.saveAppUser(new AppUser(null,"Daniel", "dan","1234",
//                    "ivans@mail.com",true,false, LocalDateTime.now(),new ArrayList<>(),new ArrayList<>()));


//            userService.addRoleToAppUser("alex","ROLE_MANAGER");
//            userService.addRoleToAppUser("alex","ROLE_ADMIN");
//            userService.addRoleToAppUser("alex","ROLE_SUPER_ADMIN");

        };
    }

}