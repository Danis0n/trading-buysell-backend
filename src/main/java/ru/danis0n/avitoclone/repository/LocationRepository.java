package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.advert.LocationEntity;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    Optional<LocationEntity> findById(Long id);
    LocationEntity getById(Long id);
    LocationEntity findByName(String name);
}