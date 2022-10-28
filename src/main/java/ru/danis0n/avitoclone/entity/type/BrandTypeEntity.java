package ru.danis0n.avitoclone.entity.type;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brand_type")
public class BrandTypeEntity {

    @Id
    @SequenceGenerator(
            name = "brand_type_sequence",
            sequenceName = "brand_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "brand_type_sequence"
    )
    private Long id;

    private String name;

}
