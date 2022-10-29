package ru.danis0n.avitoclone.service.type;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.entity.type.FullTypeEntity;
import ru.danis0n.avitoclone.repository.type.*;


@Service
@AllArgsConstructor
public class TypeServiceImpl implements TypeService {

    private final FullTypeRepository fullTypeRepository;
    private final MainTypeRepository mainTypeRepository;
    private final BrandTypeRepository brandTypeRepository;
    private final TitleTypeRepository titleTypeRepository;
    private final SubTypeRepository subTypeRepository;

    @Override
    public FullTypeEntity handleNewTypeForAdvert(String mainType, String brandType, String titleType, String subType) {
        FullTypeEntity type = new FullTypeEntity();
        type.setMainType(mainTypeRepository.getByName(mainType));
        type.setBrandType(brandTypeRepository.getByName(brandType));
        type.setTitleType(titleTypeRepository.getByName(titleType));
        type.setSubType(subTypeRepository.getByName(subType));
        fullTypeRepository.save(type);
        return type;
    }
}
