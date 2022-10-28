package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.entity.user.AppUserInfoEntity;

@Repository
public interface AppUserInfoRepository extends JpaRepository<AppUserInfoEntity, Long> {
    AppUserInfoEntity getByUser(AppUserEntity user);
    AppUserInfoEntity getById(Long id);
    boolean existsAppUserInfoEntityByEmail(String email);
}
