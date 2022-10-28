package ru.danis0n.avitoclone.entity.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_user_infos")
public class AppUserInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "userInfo")
    private AppUserEntity user;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreation;

    @PrePersist
    private void init(){
        dateOfCreation = LocalDateTime.now();
        rating = 0f;
    }

}

