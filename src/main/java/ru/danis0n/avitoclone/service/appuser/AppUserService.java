package ru.danis0n.avitoclone.service.appuser;


import ru.danis0n.avitoclone.dto.appuser.RegistrationRequest;
import ru.danis0n.avitoclone.dto.appuser.AppUser;
import ru.danis0n.avitoclone.dto.appuser.Role;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AppUserService {
    String saveAppUser(RegistrationRequest request);
    Role saveRole(Role role);
    AppUser getAppUser(String username);
    AppUser getAppUserById(Long id);
    AppUserEntity getAppUserEntityByUsername(String username);
    AppUserEntity getAppUserEntityById(Long id);
    List<AppUser> getAppUsers();
    void enableAppUser(String username);
    void addRoleToAppUser(AppUserEntity user, String roleName);
    void removeRoleFromAppUser(AppUserEntity user,String roleName);
    boolean isExistsAppUserEntityByEmail(String email);
    boolean isExistsAppUserEntityByUsername(String username);
    void lockAppUser(String username);
    void unLockAppUser(String username);
    String banAppUserById(Long id, HttpServletRequest request);
    String unBanAppUserById(Long id);
    String saveUserPassword(Long id, String newPassword,String oldPassword, HttpServletRequest request, HttpServletResponse response);
    String saveUserName(Long id, String name, HttpServletRequest request, HttpServletResponse response);
    String saveUserPhone(Long id, String phone, HttpServletRequest request, HttpServletResponse response);
    String saveUserEmail(Long id, String email, HttpServletRequest request, HttpServletResponse response);
}
