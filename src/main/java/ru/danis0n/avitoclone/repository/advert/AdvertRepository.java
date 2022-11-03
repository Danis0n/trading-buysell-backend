package ru.danis0n.avitoclone.repository.advert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;

import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<AdvertEntity,Long> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts WHERE is_hidden = false AND is_hidden_by_admin = false"
    )
    List<AdvertEntity> findAllUnHidden();

    void deleteById(Long id);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts WHERE user_id = ?1"
    )
    List<AdvertEntity> findAllByUserId(Long userId);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts ORDER BY date_of_creation DESC LIMIT ?1"
    )
    List<AdvertEntity> findAllLatest(Long quantity);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts ORDER BY random() LIMIT 6"
    )
    List<AdvertEntity> findExamples();
    @Transactional
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE adverts SET is_hidden_by_admin = ?1 WHERE user_id = ?2"
    )
    void hideAllAdvertsByUserIdAdmin(Boolean statement, Long userId);
    @Transactional
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE adverts SET is_hidden_by_admin = ?1 WHERE user_id = ?2 AND id = ?3"
    )
    void hideAdvertByUserIdAdmin(Boolean statement, Long userId, Long advertId);
    @Transactional
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE adverts SET is_hidden = ?1 WHERE user_id = ?2"
        )
    void hideAllAdvertsByUserId(Boolean statement, Long userId);
    @Transactional
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE adverts SET is_hidden = ?1 WHERE user_id = ?2 AND id = ?3"
        )
    void hideAdvertByUserId(Boolean statement, Long userId, Long advertId);

}


