package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.advert.AdvertTypeEntity;

@Repository
public interface AdvertTypeRepository extends JpaRepository<AdvertTypeEntity,Long> {
    AdvertTypeEntity findByType(String type);
}
