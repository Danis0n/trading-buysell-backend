package ru.danis0n.avitoclone.service.admin;

import javax.servlet.http.HttpServletRequest;

public interface AdminService {
    String banAppUserById(Long id, HttpServletRequest request);
    String unBanAppUserById(Long id, HttpServletRequest request);
    String powerDeleteAdvert(Long id, HttpServletRequest request);
    String notifyUser(Long id, HttpServletRequest request);
    void lockAppUser(Long id);
    void unLockAppUser(Long id);
    String hideUsersAdverts(Long id);
}
