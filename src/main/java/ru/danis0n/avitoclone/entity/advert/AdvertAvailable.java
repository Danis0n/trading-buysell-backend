package ru.danis0n.avitoclone.entity.advert;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(
        name = "advertAvailableSqlResult",
        entities = @EntityResult(entityClass = AdvertAvailable.class)
)
public class AdvertAvailable {
    @Id
    private Long id;
    private String quantity;
}
