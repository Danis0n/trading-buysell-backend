package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.AdvertEntity;
import ru.danis0n.avitoclone.entity.AdvertTypeEntity;

import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<AdvertEntity,Long> {
    AdvertEntity findByTitle(String title);
    List<AdvertEntity> findAllByType(AdvertTypeEntity type);
}
