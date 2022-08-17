package ru.danis0n.avitoclone.service.appuser;


import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.entity.AppUserEntity;

import java.util.List;

public interface AppUserService {
    String saveAppUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToAppUser(String username, String roleName);
    void removeRoleFromAppUser(String username,String roleName);
    AppUser getAppUser(String username);
    AppUserEntity getAppUserEntity(String username);
    List<AppUser> getAppUsers();
    void enabledAppUser(String email);
    boolean existsAppUserEntityByEmail(String email);
    boolean existsAppUserEntityByUsername(String username);
    AppUser getAppUserById(Long id);
}
