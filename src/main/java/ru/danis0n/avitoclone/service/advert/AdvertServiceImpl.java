package ru.danis0n.avitoclone.service.advert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.Advert;
import ru.danis0n.avitoclone.dto.AdvertType;
import ru.danis0n.avitoclone.entity.AdvertEntity;
import ru.danis0n.avitoclone.entity.AdvertTypeEntity;
import ru.danis0n.avitoclone.entity.ImageEntity;
import ru.danis0n.avitoclone.repository.AdvertRepository;
import ru.danis0n.avitoclone.repository.AdvertTypeRepository;
import ru.danis0n.avitoclone.service.image.ImageService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdvertServiceImpl implements AdvertService{

    private final AdvertRepository advertRepository;
    private final AdvertTypeRepository advertTypeRepository;
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

    @Override
    public String create(HttpServletRequest request, String title, String location, String description, String price, MultipartFile[] files) {
        return null;
    }

    @Override
    public void createType(AdvertType advertType) {
        AdvertTypeEntity type = new AdvertTypeEntity();
        type.setType(advertType.getType());
        advertTypeRepository.save(type);
    }
}
