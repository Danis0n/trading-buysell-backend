package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;
import ru.danis0n.avitoclone.entity.advert.AdvertTypeEntity;
import ru.danis0n.avitoclone.entity.AppUserEntity;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<AdvertEntity,Long> {
    AdvertEntity findByTitle(String title);
    List<AdvertEntity> findAllByType(AdvertTypeEntity type);
    List<AdvertEntity> findAllByUser(AppUserEntity user);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts WHERE SIMILARITY(title, ?1) > 0.1 ")
    List<AdvertEntity> findByTitleSmart(String title);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts WHERE price BETWEEN ?1 AND ?2")
    List<AdvertEntity> findAllByPriceSmart(BigDecimal less, BigDecimal greater);

}


