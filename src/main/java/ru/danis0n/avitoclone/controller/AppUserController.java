package ru.danis0n.avitoclone.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.danis0n.avitoclone.dto.appuser.AppUser;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.service.appuser.AppUserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers(){
        return ResponseEntity.ok().body(userService.getAppUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.getAppUserById(id));
    }

    @PostMapping("/users/ban/{id}")
    public ResponseEntity<String> banUser(@PathVariable String id, HttpServletRequest request){
        return ResponseEntity.ok().body(userService.banAppUserById(id,request));
    }

    @PostMapping("/users/unban/{id}")
    public ResponseEntity<String> unBanUser(@PathVariable String id){
        return ResponseEntity.ok().body(userService.unBanAppUserById(id));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/user/save").toUriString());

        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

//    @PostMapping("/role/add/user")
//    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form){
//        userService.addRoleToAppUser(form.getRoleName(), form.getRoleName());
//        return ResponseEntity.ok().build();
//    }
}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}
