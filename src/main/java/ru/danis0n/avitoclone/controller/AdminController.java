package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.danis0n.avitoclone.service.admin.AdminService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/users/ban/{id}")
    public ResponseEntity<String> banUser(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok().body(adminService.banAppUserById(id, request));
    }

    @PostMapping("/users/unban/{id}")
    public ResponseEntity<String> unBanUser(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok().body(adminService.unBanAppUserById(id,request));
    }

    @PostMapping("/adverts/power/delete/{id}")
    public ResponseEntity<String> powerDeleteAdvert(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok().body(adminService.powerDeleteAdvert(id,request));
    }

    @PostMapping("/users/notify/{id}")
    public ResponseEntity<String> notifyUser(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok().body(adminService.notifyUser(id,request));
    }

//    TODO : implement redirect for email confirmation
    @PostMapping("/redirect")
    public RedirectView redirect(){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("https://ru.lipsum.com");
        return redirectView;
    }

}
