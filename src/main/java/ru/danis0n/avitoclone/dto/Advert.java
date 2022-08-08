package ru.danis0n.avitoclone.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Advert {

    private Long id;
    private String title;
    private String price;
    private String description;
    private LocalDateTime dateOfCreation;
    List<Image> images = new ArrayList<>();

    public void addImageToAdvert(Image image){
        images.add(image);
    }

}
