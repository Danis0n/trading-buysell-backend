package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> getByAppUser(AppUserEntity appUser);
    void deleteAllByAppUser(AppUserEntity appUser);
}