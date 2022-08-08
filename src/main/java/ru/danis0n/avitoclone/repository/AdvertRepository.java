package ru.danis0n.avitoclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.AdvertEntity;

@Repository
public interface AdvertRepository extends JpaRepository<AdvertEntity,Long> {

}
