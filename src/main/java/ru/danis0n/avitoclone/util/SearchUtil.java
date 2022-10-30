package ru.danis0n.avitoclone.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.advert.Available;
import ru.danis0n.avitoclone.entity.advert.AdvertAvailable;
import ru.danis0n.avitoclone.dto.advert.AdvertSearchRequest;
import ru.danis0n.avitoclone.dto.type.TypeRequest;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;
import ru.danis0n.avitoclone.entity.type.BrandTypeEntity;
import ru.danis0n.avitoclone.entity.type.MainTypeEntity;
import ru.danis0n.avitoclone.entity.type.SubTypeEntity;
import ru.danis0n.avitoclone.entity.type.TitleTypeEntity;
import ru.danis0n.avitoclone.repository.type.BrandTypeRepository;
import ru.danis0n.avitoclone.repository.type.MainTypeRepository;
import ru.danis0n.avitoclone.repository.type.SubTypeRepository;
import ru.danis0n.avitoclone.repository.type.TitleTypeRepository;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
    private final ObjectMapperUtil mapperUtil;
    private final BrandTypeRepository brandTypeRepository;
    private final MainTypeRepository mainTypeRepository;
    private final SubTypeRepository subTypeRepository;
    private final TitleTypeRepository titleTypeRepository;

    public List<AdvertEntity> getByParams(HttpServletRequest request) {

        AdvertSearchRequest searchRequest = getSearchRequest(request);
        TypeRequest typeRequest = searchRequest.getType();

        String sql = "advertSqlResult";
        StringBuilder query = new StringBuilder("SELECT * FROM adverts \n").append(setJoinIfNecessary(typeRequest));

        query.
                append(getPriceForQuery(searchRequest.getMinPrice(),searchRequest.getMaxPrice())).append(" AND ").
                append(getTypesForQuery(typeRequest)).
                append(getTitleForQuery(searchRequest.getTitle())).
                append(getLocationForQuery(searchRequest.getLocation())).
                append(" AND is_hidden = false").
                append(" AND is_hidden_by_admin = false");

        log.info(query.toString());
        return getAllByNativeQuery(String.valueOf(query),sql);
    }

    public List<Available> getAvailableQuantity(HttpServletRequest request) {

        String query =
                "SELECT brand_type_id AS id ," +
                " COUNT(brand_type_id) AS quantity" +
                " FROM adverts \n JOIN types\n" +
                "\tON adverts.type_id = types.id\n" +
                "WHERE " + getTypesForQuery(getTypeRequest(request)) +
                "AND is_hidden = false \n" +
                "AND is_hidden_by_admin = false \n" +
                "GROUP BY brand_type_id";
        String sql = "advertAvailableSqlResult";

        return mapperUtil.mapToAvailable(getBrandQuantity(query,sql));
    }

    private List<AdvertEntity> getAllByNativeQuery(String query, String sql) {
        return em.createNativeQuery(query,sql).getResultList();
    }

    private List<AdvertAvailable> getBrandQuantity(String query, String sql) {
        return em.createNativeQuery(query,sql).getResultList();
    }

    private StringBuilder getTypesForQuery(TypeRequest typeRequest) {

        if (!isTypeInSearch(typeRequest)) return new StringBuilder();

        StringBuilder queryPart = new StringBuilder();

        List<TitleTypeEntity> titleType = new ArrayList<>();
        List<MainTypeEntity> mainTypes = new ArrayList<>();
        List<SubTypeEntity> subType = new ArrayList<>();
        List<BrandTypeEntity> brandType = new ArrayList<>();

        setTypeToLists(typeRequest,titleType,mainTypes,subType,brandType);
        queryPart.append(setQueryWithTypes(titleType,mainTypes,subType,brandType));
        return queryPart;
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

    private void setTypeToLists(TypeRequest typeRequest,
                                List<TitleTypeEntity> titleType,
                                List<MainTypeEntity> mainTypes,
                                List<SubTypeEntity> subType,
                                List<BrandTypeEntity> brandType) {

        if(!isTypeInSearch(typeRequest)) return;

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
                "\tON adverts.type_id = types.id \n WHERE " : " WHERE ";
    }

    private boolean isTypeInSearch(TypeRequest typeRequest) {
        return !typeRequest.getTitleType().equals("none");
    }

    private StringBuilder getPriceForQuery(BigDecimal minPrice, BigDecimal maxPrice) {
        return new StringBuilder(String.format("price >= %s AND price <= %s ", minPrice.toString(), maxPrice.toString()));
    }

    private String getTitleForQuery(String title) {
        return isTitle(title) ? String.format(" AND SIMILARITY(title, '%s' ) > 0.1", title) : "";
    }

    private String getLocationForQuery(String location) {
        return isLocation(location) ? String.format(" AND SIMILARITY(location, '%s' ) > 0.1", location) : "";
    }

    private boolean isTitle(String title) {
        return !title.equals("none");
    }

    private boolean isLocation(String location) {
        return !location.equals("none");
    }

    private AdvertSearchRequest getSearchRequest(HttpServletRequest request){
        String jsonBody = jsonUtil.getJson(request);
        return jsonUtil.getGson().
                fromJson(jsonBody, AdvertSearchRequest.class);
    }

    private TypeRequest getTypeRequest(HttpServletRequest request){
        String jsonBody = jsonUtil.getJson(request);
        return jsonUtil.getGson().
                fromJson(jsonBody, TypeRequest.class);
    }
}
