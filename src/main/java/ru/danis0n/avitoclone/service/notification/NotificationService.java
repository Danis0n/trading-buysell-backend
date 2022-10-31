package ru.danis0n.avitoclone.service.notification;

import ru.danis0n.avitoclone.dto.notification.Notification;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface NotificationService {
    List<Notification> getAllByUserId(Long id, HttpServletRequest request);
    String setNotificationViewed(Long id, HttpServletRequest request);
    String deleteNotificationById(Long id, HttpServletRequest request);
    String getAllUnViewedNotificationsByUserId(Long id, HttpServletRequest request);
}
