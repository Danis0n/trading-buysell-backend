package ru.danis0n.avitoclone.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUserEntity appUser;

    @Column(name = "token")
    private String token;

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreation;

    @PrePersist
    private void init(){
        dateOfCreation = LocalDateTime.now();
    }

}
