package ru.danis0n.avitoclone.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Advert {

    private Long id;
    private String title;
    private String price;
    private String location;
    private String description;
    private Long userId;
    private AdvertType type;
    private String dateOfCreation;
    List<Image> images = new ArrayList<>();

    public void addImageToAdvert(Image image){
        images.add(image);
    }

}
