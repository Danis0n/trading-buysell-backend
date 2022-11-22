package ru.danis0n.avitoclone.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.danis0n.avitoclone.dto.appuser.AppUser;
import ru.danis0n.avitoclone.dto.appuser.Role;
import ru.danis0n.avitoclone.service.appuser.AppUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService userService;

    @GetMapping("/users/get/all")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(userService.getAppUsers());
    }

    @GetMapping("/users/get/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getAppUserById(id));
    }

    @PutMapping("/users/{id}/password/save")
    public ResponseEntity<String> saveUserPassword(@PathVariable Long id,
                                                   @RequestParam("newPassword") String newPassword,
                                                   @RequestParam("oldPassword") String oldPassword,
                                                   HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok().body(userService.saveUserPassword(id,newPassword,oldPassword,request,response));
    }

    @PutMapping("/users/{id}/name/save")
    public ResponseEntity<String> saveUserName(@PathVariable Long id, @RequestParam("name") String name,
                                               HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok().body(userService.saveUserName(id,name,request,response));
    }

    @PutMapping("/users/{id}/phone/save")
    public ResponseEntity<String> saveUserPhone(@PathVariable Long id, @RequestParam("phone") String phone,
                                                HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok().body(userService.saveUserPhone(id,phone,request,response));
    }

    @PutMapping("/users/{id}/email/save")
    public ResponseEntity<String> saveUserEmail(@PathVariable Long id, @RequestParam("email") String email,
                                                HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok().body(userService.saveUserEmail(id,email,request,response));
    }

    @PostMapping("/users/{id}/image/save")
    public ResponseEntity<Boolean> saveUserImage(@PathVariable Long id, @RequestParam("file") MultipartFile file,
                                                HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok().body(userService.saveUserImage(id,file,request,response));
    }

    @DeleteMapping("/users/{id}/image/delete")
    public ResponseEntity<Boolean> deleteUserImage(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok().body(userService.deleteUserImage(id,request,response));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/user/save").toUriString());

        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }
}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}
