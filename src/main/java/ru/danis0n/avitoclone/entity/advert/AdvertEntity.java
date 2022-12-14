package ru.danis0n.avitoclone.entity.advert;

import lombok.*;
import ru.danis0n.avitoclone.entity.type.FullTypeEntity;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "adverts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(
        name = "advertSqlResult",
        entities = @EntityResult(entityClass = AdvertEntity.class)
)
public class AdvertEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @Column(name = "is_hidden")
    private Boolean isHidden;

    @Column(name = "is_hidden_by_admin")
    private Boolean isHiddenByAdmin;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @OneToOne
    @JoinColumn(
            nullable = true,
            name = "location_id"
    )
    private LocationEntity location;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "type_id"
    )
    private FullTypeEntity type;

    @ManyToOne(
            cascade = CascadeType.REFRESH,
            fetch = FetchType.EAGER
    )
    private AppUserEntity user;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "advert"
    )
    private Set<ImageEntity> images = new HashSet<>();

    public void addImageToAd(ImageEntity image){
        images.add(image);
    }

    @PrePersist
    private void initTime(){
        dateOfCreation = LocalDateTime.now();
    }

    public void clearList(){
        images.clear();
    }

}