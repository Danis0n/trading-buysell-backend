package ru.danis0n.avitoclone.repository.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.type.SubTypeEntity;

@Repository
public interface SubTypeRepository extends JpaRepository<SubTypeEntity, Long> {
    SubTypeEntity getByName(String name);
    SubTypeEntity getById(Long id);
}
