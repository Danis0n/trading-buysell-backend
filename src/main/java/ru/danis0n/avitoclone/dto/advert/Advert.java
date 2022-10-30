package ru.danis0n.avitoclone.dto.advert;

import lombok.*;
import ru.danis0n.avitoclone.dto.type.FullType;

import java.math.BigDecimal;
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
    private BigDecimal price;
    private String location;
    private String description;
    private Long userId;
    private Boolean isHidden;
    private FullType type;
    private String dateOfCreation;
    List<Image> images = new ArrayList<>();

    public void addImageToAdvert(Image image){
        images.add(image);
    }

}
