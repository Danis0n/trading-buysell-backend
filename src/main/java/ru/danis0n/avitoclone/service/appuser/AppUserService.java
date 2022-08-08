package ru.danis0n.avitoclone.service.appuser;


import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.RoleEntity;

import java.util.List;

public interface AppUserService {
    AppUser saveAppUser(AppUser user);

    Role saveRole(Role role);

    void addRoleToAppUser(String username, String roleName);

    AppUser getAppUser(String username);

    List<AppUser> getAppUsers();
}
