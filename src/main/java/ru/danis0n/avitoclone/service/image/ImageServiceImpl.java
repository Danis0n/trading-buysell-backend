package ru.danis0n.avitoclone.service.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.danis0n.avitoclone.dto.Image;
import ru.danis0n.avitoclone.entity.ImageEntity;


@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Override
    public Image mapToImage(ImageEntity entity) {
        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/display/")
                .path(entity.getId())
                .toUriString();
        Image image = new Image();
        image.setId(entity.getId());
        image.setName(entity.getName());
        image.setContentType(entity.getContentType());
        image.setSize(entity.getSize());
        image.setUrl(downloadURL);
        return image;
    }

}
