package ru.danis0n.avitoclone.entity.advert;

import lombok.*;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.ImageEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "adverts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "location")
    private String location;

    @Column(name = "price")
    private String price;

    @Column(name = "description")
    private String description;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "type_id"
    )
    private AdvertTypeEntity type;

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