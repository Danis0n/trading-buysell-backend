package ru.danis0n.avitoclone.repository.advert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;

import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<AdvertEntity,Long> {

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
    @Query(
            nativeQuery = true,
            value = "UPDATE adverts SET is_hidden_by_admin = ?2 WHERE user_id = ?1"
    )
    void hideAllAdvertsByUserIdAdmin(Long userId, Boolean statement);
    @Query(
            nativeQuery = true,
            value = "UPDATE adverts SET is_hidden_by_admin = ?3 WHERE user_id = ?1 AND id = ?2"
    )
    void hideAdvertByUserIdAdmin(Long userId, Long advertId, Boolean statement);
    @Query(
            nativeQuery = true,
            value = "UPDATE adverts SET is_hidden = ?2 WHERE user_id = ?1"
        )
    void hideAllAdvertsByUserId(Long userId, Boolean statement);
    @Query(
            nativeQuery = true,
            value = "UPDATE adverts SET is_hidden = ?3 WHERE user_id = ?1 AND id = ?2"
        )
    void hideAdvertByUserId(Long userId, Long advertId, Boolean statement);

}


