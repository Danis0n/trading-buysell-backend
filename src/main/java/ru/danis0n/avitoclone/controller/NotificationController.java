package ru.danis0n.avitoclone.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.danis0n.avitoclone.dto.notification.Notification;
import ru.danis0n.avitoclone.service.notification.NotificationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/get/user/{id}/all")
    public List<Notification> getAllByUserId(@PathVariable Long id, HttpServletRequest request){
        return notificationService.getAllByUserId(id,request);
    }

    @GetMapping("/get/user/{id}/unviewed")
    public String getAllUnViewedNotificationsByUserId(@PathVariable Long id, HttpServletRequest request){
        return notificationService.getAllUnViewedNotificationsByUserId(id,request);
    }

    @PostMapping("/set/notify/viewed/{id}")
    public String setNotificationViewed(@PathVariable Long id, HttpServletRequest request){
        return notificationService.setNotificationViewed(id,request);
    }

    @PostMapping("/delete/notify/{id}")
    public String deleteNotificationById(@PathVariable Long id, HttpServletRequest request){
        return notificationService.deleteNotificationById(id,request);
    }

}
