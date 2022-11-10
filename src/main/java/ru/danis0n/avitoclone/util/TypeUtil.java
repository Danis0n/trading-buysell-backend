package ru.danis0n.avitoclone.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.danis0n.avitoclone.dto.type.NewTypeRequest;
import ru.danis0n.avitoclone.entity.advert.LocationEntity;
import ru.danis0n.avitoclone.entity.type.BrandTypeEntity;
import ru.danis0n.avitoclone.entity.type.MainTypeEntity;
import ru.danis0n.avitoclone.entity.type.SubTypeEntity;
import ru.danis0n.avitoclone.repository.LocationRepository;
import ru.danis0n.avitoclone.repository.type.BrandTypeRepository;
import ru.danis0n.avitoclone.repository.type.MainTypeRepository;
import ru.danis0n.avitoclone.repository.type.SubTypeRepository;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Slf4j
@Component
@AllArgsConstructor
public class TypeUtil {

    private TransactionTemplate transactionTemplate;
    private EntityManager em;
    private final JsonUtil jsonUtil;
    private final BrandTypeRepository brandTypeRepository;
    private final SubTypeRepository subTypeRepository;
    private final MainTypeRepository mainTypeRepository;
    private final LocationRepository locationRepository;

    public String addNewType(String id, HttpServletRequest request) {
        NewTypeRequest typeRequest = getNewTypeRequest(request);

        switch (typeRequest.getType()) {
            case "brand": {
                BrandTypeEntity type = new BrandTypeEntity();
                type.setName(typeRequest.getName());
                brandTypeRepository.save(type);
                break;
            }
            case "main": {
                MainTypeEntity type = new MainTypeEntity();
                type.setName(typeRequest.getName());
                mainTypeRepository.save(type);
                break;
            }
            case "sub": {
                SubTypeEntity type = new SubTypeEntity();
                type.setName(typeRequest.getName());
                subTypeRepository.save(type);
                break;
            }
            case "loc": {
                LocationEntity type = new LocationEntity();
                type.setName(typeRequest.getName());
                type.setDescription(typeRequest.getDescription());
                locationRepository.save(type);
                break;
            }
        }

        return id.equals("location") ? "Success" : doQuery(typeRequest,id);
    }

    private String doQuery(NewTypeRequest typeRequest, String id) {
        int typeId = new Random().nextInt(1000) * typeRequest.getDescription().length() / 10;
        String query =
                "INSERT INTO " + typeRequest.getType() + "_" + id + "_type" + " VALUES" +
                        "(" + typeId + ", '" + typeRequest.getName() + "', '" +
                        typeRequest.getDescription() + "');";
        doNativeQuery(query);
        return "Success";
    }

    void doNativeQuery(String query) {
        transactionTemplate.execute(transactionStatus -> {
           em.createNativeQuery(query).executeUpdate();
           transactionStatus.flush();
           return null;
        });
    }

    private NewTypeRequest getNewTypeRequest(HttpServletRequest request){
        String jsonBody = jsonUtil.getJson(request);
        return jsonUtil.getGson().
                fromJson(jsonBody, NewTypeRequest.class);
    }

}
