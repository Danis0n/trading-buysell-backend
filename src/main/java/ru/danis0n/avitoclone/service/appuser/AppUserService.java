package ru.danis0n.avitoclone.service.appuser;


import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Role;

import java.util.List;

public interface AppUserService {
    String saveAppUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToAppUser(String username, String roleName);
    void removeRoleFromAppUser(String username,String roleName);
    AppUser getAppUser(String username);
    List<AppUser> getAppUsers();
    void enabledAppUser(String email);
}
