package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity,Long> {
    AppUserEntity findByUsername(String username);
    boolean existsAppUserEntityByUsername(String username);
    @Transactional
    @Modifying
    @Query("UPDATE AppUserEntity a " +
            "SET a.enabled = TRUE WHERE a.username = ?1")
    int enableAppUser(String username);

    @Transactional
    @Modifying
    @Query("UPDATE AppUserEntity a " +
            "SET a.locked = TRUE WHERE a.username = ?1")
    void lockAppUser(String username);

    @Transactional
    @Modifying
    @Query("UPDATE AppUserEntity a " +
            "SET a.locked = FALSE WHERE a.username = ?1")
    void unLockAppUser(String username);

}