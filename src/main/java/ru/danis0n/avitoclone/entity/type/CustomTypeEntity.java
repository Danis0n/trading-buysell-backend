package ru.danis0n.avitoclone.entity.type;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;

import javax.persistence.*;

@Entity
@Table(name = "custom_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(
        name = "custom_type_result",
        entities = @EntityResult(entityClass = CustomTypeEntity.class)
)
public class CustomTypeEntity {

    @Id
    private Long id;
    private String name;
    private String description;

}
