package ru.danis0n.avitoclone.entity.type;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sub_sub_types")
public class SubSubTypeEntity {

    @Id
    @SequenceGenerator(
            name = "sub_sub_type_sequence",
            sequenceName = "sub_sub_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sub_sub_type_sequence"
    )
    private Long id;

    private String name;

}
