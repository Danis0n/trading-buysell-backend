package ru.danis0n.avitoclone.service.confirm;

import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.entity.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken token);
    Optional<ConfirmationToken> getTokenByAppUser(AppUserEntity appUser);
    Optional<ConfirmationToken> getToken(String token);
    int setConfirmedAt(String token);
}
