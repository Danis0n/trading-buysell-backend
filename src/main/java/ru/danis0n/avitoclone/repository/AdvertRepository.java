package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.dto.advert.Advert;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;
import ru.danis0n.avitoclone.entity.advert.AdvertTypeEntity;
import ru.danis0n.avitoclone.entity.AppUserEntity;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<AdvertEntity,Long> {
    List<AdvertEntity> findAllByUser(AppUserEntity user);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts ORDER BY date_of_creation DESC LIMIT ?1"
    )
    List<AdvertEntity> findAllLatest(Long quantity);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts WHERE SIMILARITY(title, ?1) > 0.1 ")
    List<AdvertEntity> findByTitleSmart(String title);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts WHERE price BETWEEN ?1 AND ?2")
    List<AdvertEntity> findAllByPriceSmart(BigDecimal less, BigDecimal greater);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts" +
                    " WHERE" +
                    " SIMILARITY(title, ?1) > 0.1" +
                    " AND" +
                    " type_id = ?2" +
                    " AND" +
                    " SIMILARITY(location, ?3) > 0.1" +
                    " AND" +
                    " price BETWEEN ?4 AND ?5 "
    )
    List<AdvertEntity> findAllByFullSearch(String title, Long type, String location, BigDecimal minPrice, BigDecimal maxPrice);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts" +
                    " WHERE" +
                    " SIMILARITY(location, ?1) > 0.1" +
                    " AND" +
                    " price BETWEEN ?2 AND ?3 "
    )
    List<AdvertEntity> findAllByLocation(String location, BigDecimal minPrice, BigDecimal maxPrice);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts" +
                    " WHERE" +
                    " SIMILARITY(title, ?1) > 0.1" +
                    " AND" +
                    " price BETWEEN ?2 AND ?3 "
    )
    List<AdvertEntity> findAllByTitle(String title, BigDecimal minPrice, BigDecimal maxPrice);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts" +
                    " WHERE" +
                    " type_id = ?1"
    )
    List<AdvertEntity> findAllByType(Long typeId);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts" +
                    " WHERE" +
                    " type_id = ?1" +
                    " AND" +
                    " SIMILARITY(location, ?2) > 0.1" +
                    " AND" +
                    " price BETWEEN ?3 AND ?4 "
    )
    List<AdvertEntity> findAllByTypeAndLocation(Long typeId, String location, BigDecimal minPrice, BigDecimal maxPrice);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts" +
                    " WHERE" +
                    " type_id = ?1" +
                    " AND" +
                    " SIMILARITY(title, ?2) > 0.1" +
                    " AND" +
                    " price BETWEEN ?3 AND ?4 "
    )
    List<AdvertEntity> findAllByTypeAndTitle(Long typeId, String title, BigDecimal minPrice, BigDecimal maxPrice);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts" +
                    " WHERE" +
                    " SIMILARITY(title, ?1) > 0.1" +
                    " AND" +
                    " SIMILARITY(location, ?2) > 0.1" +
                    " AND" +
                    " price BETWEEN ?3 AND ?4 "
    )
    List<AdvertEntity> findAllByTitleAndLocation(String title, String location, BigDecimal minPrice, BigDecimal maxPrice);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM adverts ORDER BY random() LIMIT 6"
    )
    List<AdvertEntity> findExamples();
}


