package ru.danis0n.avitoclone.service.appuser;


import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.entity.AppUserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AppUserService {
    String saveAppUser(AppUser user);
    Role saveRole(Role role);
    AppUser getAppUser(String username);
    AppUser getAppUserById(Long id);
    AppUserEntity getAppUserEntity(String username);
    List<AppUser> getAppUsers();
    void enableAppUser(String email);
    void addRoleToAppUser(String username, String roleName);
    void removeRoleFromAppUser(String username,String roleName);
    boolean isExistsAppUserEntityByEmail(String email);
    boolean isExistsAppUserEntityByUsername(String username);
    void lockAppUser(String username);
    void unLockAppUser(String username);
    String banAppUserById(String id, HttpServletRequest request);
    String unBanAppUserById(String id);
}
