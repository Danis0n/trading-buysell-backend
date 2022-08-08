package ru.danis0n.avitoclone.service.advert;

import ru.danis0n.avitoclone.dto.Advert;
import ru.danis0n.avitoclone.entity.AdvertEntity;

public interface AdvertService {
    Advert mapToAdvert(AdvertEntity e);
}
