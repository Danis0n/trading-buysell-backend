package ru.danis0n.avitoclone.service.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.repository.user.AppUserRepository;
import ru.danis0n.avitoclone.service.appuser.AppUserService;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

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

        manageBanned(user,"BAN");
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

        manageBanned(user,"UNBAN");
        return "user '" + user.getUsername() + "' un-banned!";
    }

    private void manageBanned(AppUserEntity user, String bannedOrNot){
        user.getRoles().clear();
        switch (bannedOrNot){
            case "BAN":{
                lockAppUser(user.getId());
                appUserService.addRoleToAppUser(user,"ROLE_BANNED");
                break;
            }
            case "UNBAN":{
                unLockAppUser(user.getId());
                appUserService.addRoleToAppUser(user,"ROLE_USER");
                break;
            }
            default:{
            }
        }
    }

    @Override
    public void enableAppUser(Long id) {
        appUserRepository.enableAppUser(id);
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
    public String powerDeleteAdvert(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public String notifyUser(Long id, HttpServletRequest request) {
        return null;
    }

}
