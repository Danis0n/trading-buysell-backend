package ru.danis0n.avitoclone.entity.advert;

import lombok.*;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(
            cascade = CascadeType.REFRESH,
            fetch = FetchType.EAGER
    )
    private AppUserEntity to;

    @ManyToOne(
            cascade = CascadeType.REFRESH,
            fetch = FetchType.EAGER
    )
    private AppUserEntity createdBy;

    @Column(name = "advert_name")
    private String advertName;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private float rating;
}


