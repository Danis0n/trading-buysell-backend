package ru.danis0n.avitoclone.service.image;

import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.Image;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;
import ru.danis0n.avitoclone.entity.ImageEntity;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    Image mapToImage(ImageEntity image);
    void clearImageListByAd(AdvertEntity advert);
    List<Image> getAllFiles();
    ImageEntity getSrcFileById(String id);
    Image getFileById(String id);
    ImageEntity saveFile(MultipartFile file, AdvertEntity advert) throws IOException;
}
