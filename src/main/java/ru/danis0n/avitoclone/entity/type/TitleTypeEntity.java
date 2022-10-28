package ru.danis0n.avitoclone.entity.type;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "title_types")
public class TitleTypeEntity {

    @Id
    @SequenceGenerator(
            name = "title_type_sequence",
            sequenceName = "title_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "title_type_sequence"
    )
    private Long id;

    private String name;

}
