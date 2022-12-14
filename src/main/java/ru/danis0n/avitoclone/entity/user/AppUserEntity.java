package ru.danis0n.avitoclone.entity.user;


import lombok.*;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;
import ru.danis0n.avitoclone.entity.advert.ImageEntity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_id", referencedColumnName = "id")
    private AppUserInfoEntity userInfo;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "locked")
    private boolean locked;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    private Collection<RoleEntity> roles = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.REFRESH,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    private Set<AdvertEntity> adverts = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private ImageEntity avatar;

    public void addRoleToAppUser(RoleEntity role){
        roles.add(role);
    }

    public void addAdvertToAppUser(AdvertEntity advert){
        adverts.add(advert);
    }

}