package ru.danis0n.avitoclone.util.search;

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
import ru.danis0n.avitoclone.entity.advert.LocationEntity;
import ru.danis0n.avitoclone.entity.type.BrandTypeEntity;
import ru.danis0n.avitoclone.entity.type.MainTypeEntity;
import ru.danis0n.avitoclone.entity.type.SubTypeEntity;
import ru.danis0n.avitoclone.entity.type.TitleTypeEntity;
import ru.danis0n.avitoclone.repository.LocationRepository;
import ru.danis0n.avitoclone.repository.type.BrandTypeRepository;
import ru.danis0n.avitoclone.repository.type.MainTypeRepository;
import ru.danis0n.avitoclone.repository.type.SubTypeRepository;
import ru.danis0n.avitoclone.repository.type.TitleTypeRepository;
import ru.danis0n.avitoclone.util.JsonUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

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
    private final LocationRepository locationRepository;

    public List<AdvertEntity> getByParams(HttpServletRequest request) {

        AdvertSearchRequest searchRequest = getSearchRequest(request);
        TypeRequest typeRequest = searchRequest.getType();
        String and = isTypeInSearch(typeRequest) ? " AND " : "";
        String sql = "advertSqlResult";
        StringBuilder query = new StringBuilder("SELECT * FROM adverts \n").append(setJoinIfNecessary(typeRequest));
        query.
                append(getPriceForQuery(searchRequest.getMinPrice(),searchRequest.getMaxPrice())).append(and).
                append(getSettingsForSearch(typeRequest)).
                append(getTitleForQuery(searchRequest.getTitle())).
                append(" AND is_hidden = false").
                append(" AND is_hidden_by_admin = false");
        return getAllByNativeQuery(String.valueOf(query),sql);
    }

    public List<Available> getAvailableQuantityBrand(HttpServletRequest request) {
        TypeRequest typeRequest = getTypeRequest(request);
        String and = isTypeInSearch(typeRequest) ? " AND " : " ";

        String query =
                "SELECT brand_type_id AS id ," +
                        " COUNT(brand_type_id) AS quantity" +
                        " FROM adverts \n JOIN types\n" +
                        "\tON adverts.type_id = types.id\n" +
                        "WHERE " + getSettingsForSearch(typeRequest) +
                        " " + and + "is_hidden = false \n" +
                        "AND is_hidden_by_admin = false \n" +
                        "GROUP BY brand_type_id";
        String sql = "advertAvailableSqlResult";
        return mapperUtil.mapToAvailableBrand(getQuantity(query,sql));
    }

    public List<Available> getAvailableQuantitySub(HttpServletRequest request) {

        TypeRequest typeRequest = getTypeRequest(request);
        String and = isTypeInSearch(typeRequest) ? " AND " : " ";

        String query =
                "SELECT sub_type_id AS id ," +
                        " COUNT(sub_type_id) AS quantity" +
                        " FROM adverts \n JOIN types\n" +
                        "\tON adverts.type_id = types.id\n" +
                        "WHERE " + getSettingsForSearch(typeRequest) +
                        " " + and + "is_hidden = false \n" +
                        "AND is_hidden_by_admin = false \n" +
                        "GROUP BY sub_type_id";
        String sql = "advertAvailableSqlResult";
        return mapperUtil.mapToAvailableSub(getQuantity(query,sql));
    }

    public List<Available> getAvailableQuantityMain(HttpServletRequest request) {

        TypeRequest typeRequest = getTypeRequest(request);
        String and = isTypeInSearch(typeRequest) ? " AND " : " ";

        String query =
                "SELECT main_type_id AS id ," +
                        " COUNT(main_type_id) AS quantity" +
                        " FROM adverts \n JOIN types\n" +
                        "\tON adverts.type_id = types.id\n" +
                        "WHERE " + getSettingsForSearch(typeRequest) +
                        " " + and + "is_hidden = false \n" +
                        "AND is_hidden_by_admin = false \n" +
                        "GROUP BY main_type_id";
        String sql = "advertAvailableSqlResult";
        return mapperUtil.mapToAvailableMain(getQuantity(query,sql));
    }

    public List<Available> getAvailableQuantityLocation(HttpServletRequest request){
        TypeRequest typeRequest = getTypeRequest(request);
        String and = isTypeInSearch(typeRequest) ? " AND " : " ";

        String query =
                "SELECT location_id AS id ," +
                        " COUNT(location_id) AS quantity" +
                        " FROM adverts \n JOIN types\n" +
                        "\tON adverts.type_id = types.id\n" +
                        "WHERE " + getSettingsForSearch(typeRequest) +
                        " " + and + "is_hidden = false \n" +
                        "AND is_hidden_by_admin = false \n" +
                        "GROUP BY location_id";
        String sql = "advertAvailableSqlResult";
        return mapperUtil.mapToAvailableLocation(getQuantity(query,sql));
    }

    private StringBuilder getSettingsForSearch(TypeRequest typeRequest) {

        if (!isTypeInSearch(typeRequest)) return new StringBuilder();

        StringBuilder queryPart = new StringBuilder();

        List<TitleTypeEntity> titleType = new ArrayList<>();
        List<MainTypeEntity> mainTypes = new ArrayList<>();
        List<SubTypeEntity> subType = new ArrayList<>();
        List<BrandTypeEntity> brandType = new ArrayList<>();
        List<LocationEntity> locations = new ArrayList<>();

        setSettings(typeRequest,titleType,mainTypes,subType,brandType, locations);
        queryPart.append(setQueryWithSettings(titleType,mainTypes,subType,brandType, locations));

        return queryPart;
    }

    private String setQueryWithSettings(List<TitleTypeEntity> titleType, List<MainTypeEntity> mainTypes,
                                        List<SubTypeEntity> subType, List<BrandTypeEntity> brandType, List<LocationEntity> locations) {
        StringBuilder query = new StringBuilder();

        if(!titleType.isEmpty()) {
            for(TitleTypeEntity element : titleType)
                query.append(" title_type_id = ").append(element.getId());
        }
        else return "";

        if(!mainTypes.isEmpty()) {
            query.append(" AND (");
            for(MainTypeEntity element : mainTypes)
                query.append("main_type_id = ").append(element.getId()).append(" OR ");
            query = new StringBuilder(query.substring(0, query.length() - 4));
            query.append(" )");
        }

        if(!subType.isEmpty()) {
            query.append(" AND (");
            for(SubTypeEntity element: subType)
                query.append("sub_type_id = ").append(element.getId()).append(" OR ");
            query = new StringBuilder(query.substring(0, query.length() - 4));
            query.append(" )");
        }

        if(!brandType.isEmpty()) {
            query.append(" AND (");
            for(BrandTypeEntity element: brandType)
                query.append("brand_type_id = ").append(element.getId()).append(" OR ");
            query = new StringBuilder(query.substring(0, query.length() - 4));
            query.append(" )");
        }

        if(!locations.isEmpty()) {
            query.append(" AND (");
            for (LocationEntity element : locations)
                query.append("location_id = ").append(element.getId()).append(" OR ");
            query = new StringBuilder(query.substring(0, query.length() - 4));
            query.append(" ) ");
        }

        return String.valueOf(query);
    }

    private void setSettings(TypeRequest typeRequest,
                             List<TitleTypeEntity> titleType,
                             List<MainTypeEntity> mainTypes,
                             List<SubTypeEntity> subType,
                             List<BrandTypeEntity> brandType, List<LocationEntity> locations) {

        if(!isTypeInSearch(typeRequest)) return;

        titleType.add(titleTypeRepository.getByName(typeRequest.getTitleType()));

        String[] mainTypeArray = typeRequest.getMainType();
        if(mainTypeArray != null && !(mainTypeArray.length == 0))
            for(String element : mainTypeArray)
                mainTypes.add(mainTypeRepository.getByName(element));

        String[] subTypeArray = typeRequest.getSubType();
        if(subTypeArray != null && !(subTypeArray.length == 0))
            for (String element: subTypeArray)
                subType.add(subTypeRepository.getByName(element));

        String[] brandTypeArray = typeRequest.getBrandType();
        if(brandTypeArray != null && !(brandTypeArray.length == 0))
            for(String element : brandTypeArray)
                brandType.add(brandTypeRepository.getByName(element));

        String[] locationArray = typeRequest.getLocations();
        if(locationArray != null && !(locationArray.length == 0))
            for(String element : locationArray)
                locations.add(locationRepository.findByName(element));
    }

    private String setJoinIfNecessary(TypeRequest typeRequest) {
        return isTypeInSearch(typeRequest) ? "JOIN types \n" +
                "\tON adverts.type_id = types.id \n WHERE " : " WHERE ";
    }

    private boolean isTypeInSearch(TypeRequest typeRequest) {
        return !typeRequest.getTitleType().equals("");
    }

    private StringBuilder getPriceForQuery(BigDecimal minPrice, BigDecimal maxPrice) {
        return new StringBuilder(String.format("price >= %s AND price <= %s ", minPrice.toString(), maxPrice.toString()));
    }

    private String getTitleForQuery(String title) {
        return isTitle(title) ? String.format(" AND SIMILARITY(title, '%s' ) > 0.1", title) : "";
    }

    private boolean isTitle(String title) {
        return !title.equals("");
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

    private List<AdvertEntity> getAllByNativeQuery(String query, String sql) {
        return em.createNativeQuery(query,sql).getResultList();
    }

    private List<AdvertAvailable> getQuantity(String query, String sql) {
        return em.createNativeQuery(query,sql).getResultList();
    }

}
