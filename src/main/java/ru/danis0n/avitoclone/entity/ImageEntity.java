package ru.danis0n.avitoclone.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
public class ImageEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "size")
    private Long size;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] data;

    @ManyToOne(
            cascade = CascadeType.ALL ,
            fetch = FetchType.EAGER
    )
    private AdvertEntity advert;

}
