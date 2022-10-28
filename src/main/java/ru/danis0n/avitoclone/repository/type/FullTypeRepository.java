package ru.danis0n.avitoclone.repository.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danis0n.avitoclone.entity.type.FullTypeEntity;

@Repository
public interface FullTypeRepository extends JpaRepository<FullTypeEntity, Long> {

}
