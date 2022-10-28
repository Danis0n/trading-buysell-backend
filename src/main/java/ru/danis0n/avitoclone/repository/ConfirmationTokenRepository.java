package ru.danis0n.avitoclone.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.entity.token.ConfirmationToken;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByAppUser(AppUserEntity user);
    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);
}
