package ru.danis0n.avitoclone.entity.advert;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "location")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity {

    @Id
    @SequenceGenerator(
            name = "location_sequence",
            sequenceName = "location_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "location_sequence"
    )
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
}
