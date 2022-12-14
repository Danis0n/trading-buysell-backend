package ru.danis0n.avitoclone.entity.type;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "main_types")
public class MainTypeEntity {

    @Id
    @SequenceGenerator(
            name = "main_type_sequence",
            sequenceName = "main_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "main_type_sequence"
    )
    private Long id;

    private String name;

}
