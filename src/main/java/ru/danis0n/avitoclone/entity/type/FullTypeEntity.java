package ru.danis0n.avitoclone.entity.type;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "types")
public class FullTypeEntity {

    @Id
    @SequenceGenerator(
            name = "type_sequence",
            sequenceName = "type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "type_sequence"
    )
    private Long id;

//    auto, study, apartments ...
    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "type_id"
    )
    private MainTypeEntity mainType;

//    all, new, used, online , offline (courses) ...
    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "sub_type_id"
    )
    private SubTypeEntity subType;

//    programming, physics, 1-room, 2-room, bmw, audi, laptop, phone
    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "sub_sub_type_id"
    )
    private SubSubTypeEntity subSubType;

}
