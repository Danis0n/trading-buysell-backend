package ru.danis0n.avitoclone.service.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.danis0n.avitoclone.dto.Image;
import ru.danis0n.avitoclone.entity.AdvertEntity;
import ru.danis0n.avitoclone.entity.ImageEntity;
import ru.danis0n.avitoclone.repository.ImageRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public ImageEntity saveFile(MultipartFile file, AdvertEntity advert) throws IOException {
        ImageEntity image = new ImageEntity();

        image.setName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
        image.setContentType(file.getContentType());
        image.setData(file.getBytes());
        image.setSize(file.getSize());
        image.setAdvert(advert);
        imageRepository.save(image);

        return image;
    }

    @Override
    public Image mapToImage(ImageEntity entity) {
        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/display/")
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

    @Override
    public void clearImageListByAd(AdvertEntity advert) {
        imageRepository.deleteAll(advert.getImages());
    }

    @Override
    public List<Image> getAllFiles() {
        return imageRepository.findAll().stream().map(this::mapToImage).collect(Collectors.toList());
    }

    @Override
    public ImageEntity getSrcFileById(String id) {
        return imageRepository.findById(id).orElse(null);
    }

    @Override
    public Image getFileById(String id) {
        ImageEntity imageEntity = imageRepository.findById(id).orElse(null);
        return mapToImage(Objects.requireNonNull(imageEntity));
    }

}
