package ru.danis0n.avitoclone.repository.type;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.danis0n.avitoclone.entity.type.BrandTypeEntity;
import ru.danis0n.avitoclone.entity.type.MainTypeEntity;
import ru.danis0n.avitoclone.entity.type.SubTypeEntity;
import ru.danis0n.avitoclone.entity.type.TitleTypeEntity;

@SpringBootTest
public class FillDataWithTypesTest {

    @Autowired
    private BrandTypeRepository brandTypeRepository;

    @Test
    public void fillBrandTypeTest() {
        BrandTypeEntity brandTypeApple = new BrandTypeEntity();
        brandTypeApple.setName("bmw");
        brandTypeRepository.save(brandTypeApple);

        BrandTypeEntity brandTypeLG = new BrandTypeEntity();
        brandTypeLG.setName("audi");
        brandTypeRepository.save(brandTypeLG);

        BrandTypeEntity brandTypeSamsung = new BrandTypeEntity();
        brandTypeSamsung.setName("mercedes");
        brandTypeRepository.save(brandTypeSamsung);

        BrandTypeEntity brandTypeHuawei = new BrandTypeEntity();
        brandTypeHuawei.setName("chevrolet");
        brandTypeRepository.save(brandTypeHuawei);

        BrandTypeEntity brandTypeXiaomi = new BrandTypeEntity();
        brandTypeXiaomi.setName("toyota");
        brandTypeRepository.save(brandTypeXiaomi);



    }


}
