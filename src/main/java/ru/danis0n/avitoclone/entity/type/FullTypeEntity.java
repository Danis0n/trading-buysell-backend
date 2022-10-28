package ru.danis0n.avitoclone.entity.type;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "types")
public class FullTypeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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
