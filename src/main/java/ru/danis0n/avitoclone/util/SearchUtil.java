package ru.danis0n.avitoclone.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.danis0n.avitoclone.dto.advert.AdvertSearchRequest;
import ru.danis0n.avitoclone.dto.type.TypeRequest;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;
import ru.danis0n.avitoclone.entity.type.BrandTypeEntity;
import ru.danis0n.avitoclone.entity.type.MainTypeEntity;
import ru.danis0n.avitoclone.entity.type.SubTypeEntity;
import ru.danis0n.avitoclone.entity.type.TitleTypeEntity;
import ru.danis0n.avitoclone.repository.advert.AdvertRepository;
import ru.danis0n.avitoclone.repository.type.BrandTypeRepository;
import ru.danis0n.avitoclone.repository.type.MainTypeRepository;
import ru.danis0n.avitoclone.repository.type.SubTypeRepository;
import ru.danis0n.avitoclone.repository.type.TitleTypeRepository;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class SearchUtil {

    private EntityManager em;
    private final JsonUtil jsonUtil;
    private final AdvertRepository advertRepository;
    private final BrandTypeRepository brandTypeRepository;
    private final MainTypeRepository mainTypeRepository;
    private final SubTypeRepository subTypeRepository;
    private final TitleTypeRepository titleTypeRepository;

    @Transactional
    public List<AdvertEntity> getByParams(HttpServletRequest request) {

        AdvertSearchRequest searchRequest = getSearchRequest(request);
        TypeRequest typeRequest = searchRequest.getType();

        String sql = "advertSqlResult";
        String startedQuery = "SELECT * FROM adverts \n" + setJoinIfNecessary(typeRequest);

        log.info(startedQuery);

        List<TitleTypeEntity> titleType = new ArrayList<>();
        List<MainTypeEntity> mainTypes = new ArrayList<>();
        List<SubTypeEntity> subType = new ArrayList<>();
        List<BrandTypeEntity> brandType = new ArrayList<>();

        if(isTypeInSearch(typeRequest)) {
            setTypesIfNecessary(typeRequest,titleType,mainTypes,subType,brandType);
            startedQuery += setQueryWithTypes(titleType,mainTypes,subType,brandType);
        }

        log.info(startedQuery);

        String query = "SELECT * FROM adverts\n" +
                "JOIN types \n" +
                "\tON adverts.type_id = types.id\n" +
                "WHERE title_type_id = 1 ";

        return getAllByNativeQuery(query,sql);
    }

    private String setQueryWithTypes(List<TitleTypeEntity> titleType, List<MainTypeEntity> mainTypes,
                                     List<SubTypeEntity> subType, List<BrandTypeEntity> brandType ) {
        StringBuilder query = new StringBuilder();

        for(TitleTypeEntity element : titleType)
            query.append("title_type_id = ").append(element.getId());

        if(!mainTypes.isEmpty()) {
            query.append(" AND (");
            for(MainTypeEntity element : mainTypes)
                query.append("main_type_id = ").append(element.getId()).append(" OR ");
            query = new StringBuilder(query.substring(0, query.length() - 4));
            query.append(" )");

            if(!subType.isEmpty()) {
                query.append(" AND (");
                for(SubTypeEntity element: subType)
                    query.append("sub_type_id = ").append(element.getId()).append(" OR ");
                query = new StringBuilder(query.substring(0, query.length() - 4));
                query.append(" )");

                if(!brandType.isEmpty()) {
                    query.append(" AND (");
                    for(BrandTypeEntity element: brandType)
                        query.append("brand_type_id = ").append(element.getId()).append(" OR ");
                    query = new StringBuilder(query.substring(0, query.length() - 4));
                    query.append(" )");
                }
            }

        }

        return String.valueOf(query);
    }

    private void setTypesIfNecessary(TypeRequest typeRequest,
                                     List<TitleTypeEntity> titleType,
                                     List<MainTypeEntity> mainTypes,
                                     List<SubTypeEntity> subType,
                                     List<BrandTypeEntity> brandType) {

        titleType.add(titleTypeRepository.getByName(typeRequest.getTitleType()));

        String[] mainTypeArray = typeRequest.getMainType();

        if(!mainTypeArray[0].equals("none")){
            for(String element : mainTypeArray)
                mainTypes.add(mainTypeRepository.getByName(element));

            String[] subTypeArray = typeRequest.getSubType();
            if(!subTypeArray[0].equals("none")) {

                for (String element: subTypeArray)
                    subType.add(subTypeRepository.getByName(element));

                String[] brandTypeArray = typeRequest.getBrandType();
                if(!brandTypeArray[0].equals("none"))
                    for(String element : brandTypeArray)
                        brandType.add(brandTypeRepository.getByName(element));

            }
        }



    }

    private String setJoinIfNecessary(TypeRequest typeRequest) {
        return isTypeInSearch(typeRequest) ? "JOIN types \n" +
                "\tON adverts.type_id = types.id \n WHERE " : "WHERE ";
    }

    private boolean isTypeInSearch(TypeRequest typeRequest) {
        return !typeRequest.getTitleType().equals("none");
    }

    private List<AdvertEntity> getAllByNativeQuery(String query, String sql) {
        return em.createNativeQuery(query,sql).getResultList();
    }

    private AdvertSearchRequest getSearchRequest(HttpServletRequest request){
        String jsonBody = jsonUtil.getJson(request);
        return jsonUtil.getGson().
                fromJson(jsonBody, AdvertSearchRequest.class);
    }
}
