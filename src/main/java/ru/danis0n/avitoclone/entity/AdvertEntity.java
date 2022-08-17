package ru.danis0n.avitoclone.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adverts")
@Getter
@Setter
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
            cascade = CascadeType.ALL ,
            fetch = FetchType.EAGER
    )
    private AppUserEntity user;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "advert"
    )
    private List<ImageEntity> images = new ArrayList<>();

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

    public AdvertEntity(){
    }

    @Override
    public String toString() {
        return "AdvertEntity{" +
                "id=" + id +
                ", title='" + title + "\n'" +
                ", location='" + location + "\n'" +
                ", price='" + price + "\n'" +
                ", description='" + description + "\n'" +
                ", dateOfCreation=" + dateOfCreation +
                ", type=" + type +
                ", user=" + user +
                ", images=" + images +
                '}';
    }
}