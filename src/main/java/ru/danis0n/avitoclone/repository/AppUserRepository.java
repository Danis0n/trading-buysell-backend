package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.AppUserEntity;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity,Long> {
    AppUserEntity findByUsername(String username);
    AppUserEntity findByEmail(String email);
    boolean existsAppUserEntityByEmail(String email);
    boolean existsAppUserEntityByUsername(String username);
}
