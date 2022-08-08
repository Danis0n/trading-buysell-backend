package ru.danis0n.avitoclone.service.advert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danis0n.avitoclone.dto.Advert;
import ru.danis0n.avitoclone.entity.AdvertEntity;
import ru.danis0n.avitoclone.entity.ImageEntity;
import ru.danis0n.avitoclone.repository.AdvertRepository;
import ru.danis0n.avitoclone.service.image.ImageService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdvertServiceImpl implements AdvertService{

    private final AdvertRepository advertRepository;
    private final ImageService imageService;

    @Override
    public Advert mapToAdvert(AdvertEntity entity) {
        Advert ad = new Advert();

        ad.setId(entity.getId());
        ad.setTitle(entity.getTitle());
        ad.setPrice(entity.getPrice());
        ad.setDescription(entity.getDescription());
        ad.setDateOfCreation(entity.getDateOfCreation());

        for(ImageEntity image : entity.getImages()){
            ad.addImageToAdvert(imageService.mapToImage(image));
        }
        return ad;
    }
}
