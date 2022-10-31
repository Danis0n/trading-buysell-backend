package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.danis0n.avitoclone.entity.notification.NotificationEntity;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByUser(AppUserEntity user);
    @Transactional
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE notifications SET seen_by_user = true WHERE user_id = ?1"
    )
    void setNotificationViewed(Long id);

    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(id) AS quantity FROM notifications WHERE user_id = ?1 AND seen_by_user = false;"
    )
    int getQuantityUnViewed(Long userId);
}
