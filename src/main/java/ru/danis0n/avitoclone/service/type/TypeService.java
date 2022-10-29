package ru.danis0n.avitoclone.service.type;

import ru.danis0n.avitoclone.entity.type.FullTypeEntity;

public interface TypeService {
    public FullTypeEntity handleNewTypeForAdvert(String mainType, String brandType, String titleType, String subType);
}
