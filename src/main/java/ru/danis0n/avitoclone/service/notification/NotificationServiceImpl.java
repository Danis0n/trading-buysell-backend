package ru.danis0n.avitoclone.service.notification;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.dto.notification.Notification;
import ru.danis0n.avitoclone.entity.notification.NotificationEntity;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.repository.NotificationRepository;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.util.JwtUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final AppUserService appUserService;
    private final JwtUtil jwtUtil;
    private final ObjectMapperUtil mapperUtil;
    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> getAllByUserId(Long id, HttpServletRequest request) {
        AppUserEntity user = appUserService.getAppUserEntityById(id);
        String username = jwtUtil.getUsernameFromRequest(request);

        if(!user.getUsername().equals(username))
            return null;

        return mapperUtil.
                mapToListNotification(notificationRepository.findAllByUser(user));
    }

    @Override
    public String setNotificationViewed(Long id, HttpServletRequest request) {
        String username = jwtUtil.getUsernameFromRequest(request);
        AppUserEntity user = appUserService.getAppUserEntityByUsername(username);
        NotificationEntity notification = notificationRepository.findById(id).orElse(null);

        if(notification == null || !notification.getUser().getId().equals(user.getId()))
            return "Error!";

        notificationRepository.setNotificationViewed(id);
        return "Success!";
    }

    @Override
    public String deleteNotificationById(Long id, HttpServletRequest request) {
        String username = jwtUtil.getUsernameFromRequest(request);
        AppUserEntity user = appUserService.getAppUserEntityByUsername(username);
        NotificationEntity notification = notificationRepository.findById(id).orElse(null);

        if(notification == null || !notification.getUser().getId().equals(user.getId()))
            return "Error!";

        notificationRepository.deleteById(id);
        return "Success!";
    }

    @Override
    public String getAllUnViewedNotificationsByUserId(Long id, HttpServletRequest request) {
        String username = jwtUtil.getUsernameFromRequest(request);
        AppUserEntity user = appUserService.getAppUserEntityByUsername(username);

        if(!user.getUsername().equals(username))
            return "Error!";

        return String.valueOf(
                notificationRepository.getQuantityUnViewed(id));
    }
}
