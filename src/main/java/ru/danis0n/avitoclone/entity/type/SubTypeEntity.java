package ru.danis0n.avitoclone.entity.type;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sub_types")
public class SubTypeEntity {

    @Id
    @SequenceGenerator(
            name = "sub_type_sequence",
            sequenceName = "sub_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sub_type_sequence"
    )
    private Long id;

    private String name;

}

