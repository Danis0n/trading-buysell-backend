package ru.danis0n.avitoclone.service.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.RefreshToken;
import ru.danis0n.avitoclone.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void saveToken(AppUserEntity appUser, String token) {
        deleteToken(appUser);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAppUser(appUser);
        refreshToken.setToken(token);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void deleteToken(AppUserEntity appUser) {
        RefreshToken toDelete = refreshTokenRepository.getByAppUser(appUser).orElse(null);
        if(toDelete == null){
            return;
        }
        refreshTokenRepository.delete(toDelete);
    }

    @Override
    public boolean validateToken(AppUserEntity appUser) {
        RefreshToken refreshToken = refreshTokenRepository.getByAppUser(appUser).orElse(null);
        return refreshToken != null;
    }


}
