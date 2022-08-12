package ru.danis0n.avitoclone.service.advert;

import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.Advert;
import ru.danis0n.avitoclone.dto.AdvertType;
import ru.danis0n.avitoclone.entity.AdvertEntity;

import javax.servlet.http.HttpServletRequest;

public interface AdvertService {
    Advert mapToAdvert(AdvertEntity e);
    String create(HttpServletRequest request, String title, String location, String description, String price, MultipartFile[] files);
    void createType(AdvertType auto);
}
