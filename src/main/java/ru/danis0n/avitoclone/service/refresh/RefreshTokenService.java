package ru.danis0n.avitoclone.service.refresh;

import ru.danis0n.avitoclone.entity.AppUserEntity;

public interface RefreshTokenService {
    void saveToken(AppUserEntity appUser, String token);
    void deleteToken(AppUserEntity appUser);
    boolean validateToken(AppUserEntity appUser);
}
