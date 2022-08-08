package ru.danis0n.avitoclone.service.image;

import ru.danis0n.avitoclone.dto.Image;
import ru.danis0n.avitoclone.entity.ImageEntity;

public interface ImageService {
    Image mapToImage(ImageEntity image);
}
