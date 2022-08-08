package ru.danis0n.avitoclone.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

}