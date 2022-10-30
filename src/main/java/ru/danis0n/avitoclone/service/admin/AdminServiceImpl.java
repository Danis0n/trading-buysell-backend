package ru.danis0n.avitoclone.service.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.dto.notification.Message;
import ru.danis0n.avitoclone.entity.notification.NotificationEntity;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.repository.NotificationRepository;
import ru.danis0n.avitoclone.repository.advert.AdvertRepository;
import ru.danis0n.avitoclone.repository.user.AppUserRepository;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;
    private final NotificationRepository notificationRepository;
    private final AdvertRepository advertRepository;
    private final JsonUtil jsonUtil;

    @Override
    public String banAppUserById(Long id, HttpServletRequest request) {
        AppUserEntity user = appUserService.getAppUserEntityById(id);
        if(user == null){
            return "null";
        }

        String username = appUserService.getUsernameFromRequest(request);

        if(user.equals(appUserService.getAppUserEntityByUsername(username))){
            return "You can't ban yourself";
        }

        manageBanned(user,"BAN",request);
        return "user '" + user.getUsername() + "' banned!";
    }

    @Override
    public String unBanAppUserById(Long id, HttpServletRequest request) {
        AppUserEntity user = appUserService.getAppUserEntityById(id);
        if(user == null){
            return "null";
        }

        String username = appUserService.getUsernameFromRequest(request);

        if(user.equals(appUserService.getAppUserEntityByUsername(username))){
            return "You can't unban yourself";
        }

        manageBanned(user,"UNBAN",request);
        return "user '" + user.getUsername() + "' un-banned!";
    }

    private void manageBanned(AppUserEntity user, String bannedOrNot, HttpServletRequest request){
        user.getRoles().clear();
        switch (bannedOrNot){
            case "BAN":{
                lockAppUser(user.getId());
                hideAllAdverts(user.getId());
                appUserService.addRoleToAppUser(user,"ROLE_BANNED");
                break;
            }
            case "UNBAN":{
                unLockAppUser(user.getId());
                unLockAppUser(user.getId());
                appUserService.addRoleToAppUser(user,"ROLE_USER");
                break;
            }
            default:{
            }
        }

        Message message = getMessageRequest(request);
        if (!message.getMessage().equals("none")) {
            createNotification(message,user);
        }
    }

    private void createNotification(Message message, AppUserEntity user) {
        NotificationEntity notification = new NotificationEntity();
        notification.setUser(user);
        notification.setMessage(message.getMessage());
        notificationRepository.save(notification);
    }

    @Override
    public void lockAppUser(Long id){
        appUserRepository.lockAppUser(id);
    }

    @Override
    public void unLockAppUser(Long id){
        appUserRepository.unLockAppUser(id);
    }

    @Override
    public String hideUserAdvertByUserId(Long userId, Long advertId, HttpServletRequest request) {
        return null;
    }

    @Override
    public String unHideUserAdvertByUserId(Long userId, Long advertId, HttpServletRequest request) {
        return null;
    }

    @Override
    public String hideAllUserAdvertsByUserId(Long userId, HttpServletRequest request) {
        return null;
    }

    @Override
    public String unHideAllUserAdvertsByUserId(Long userId, HttpServletRequest request) {
        return null;
    }

    private void hideAllAdverts(Long userId) {
        advertRepository.hideAllAdvertsByUserIdAdmin(userId,true);
    }

    private void unHideAllAdverts(Long userId) {
        advertRepository.hideAllAdvertsByUserIdAdmin(userId,false);
    }

    @Override
    public String powerDeleteAdvert(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public String notifyUser(Long id, HttpServletRequest request) {
        return null;
    }

    private Message getMessageRequest(HttpServletRequest request){
        String jsonBody = jsonUtil.getJson(request);
        return jsonUtil.getGson().
                fromJson(jsonBody, Message.class);
    }

}
