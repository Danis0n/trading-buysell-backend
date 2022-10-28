package ru.danis0n.avitoclone.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.advert.AdvertSearchRequest;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;
import ru.danis0n.avitoclone.entity.advert.AdvertTypeEntity;
import ru.danis0n.avitoclone.repository.advert.AdvertRepository;
import ru.danis0n.avitoclone.repository.advert.AdvertTypeRepository;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class SearchUtil {

    private final AdvertRepository advertRepository;
    private final JsonUtil jsonUtil;
    private final ObjectMapperUtil mapperUtil;
    private final AdvertTypeRepository advertTypeRepository;

    public List<AdvertEntity> getByParams(HttpServletRequest request) {
        BigDecimal minConst = new BigDecimal(50);
        BigDecimal maxConst = new BigDecimal(10000000);
        String emptyValue = "none";
        AdvertSearchRequest searchRequest = getSearchRequest(request);

        String title = searchRequest.getTitle();
        String type = searchRequest.getType();
        Long typeId = null;
        if(!type.equals(emptyValue)){
            typeId = findByType(type).getId();
        }
        String location = searchRequest.getLocation();
        BigDecimal minPrice = searchRequest.getMinPrice();
        BigDecimal maxPrice = searchRequest.getMaxPrice();

        if (isAllNone(title, type, location)) {
            if(minPrice.equals(minConst) && maxPrice.equals(maxConst)){
                return getAll();
            }
            else return getByPrice(minPrice,maxPrice);
        }
        else {
            Boolean isTitle = !title.equals(emptyValue);
            Boolean isType = !type.equals(emptyValue);
            Boolean isLocation = !location.equals(emptyValue);

            return getAllByOneOrMoreParams(
                    isTitle,isType,isLocation,
                    title,location,typeId,
                    minPrice,maxPrice);
        }
    }

    public List<AdvertEntity> getAll() {
        return advertRepository.findAll();
    }

    private List<AdvertEntity> getByPrice(BigDecimal less, BigDecimal greater){
        return advertRepository.findAllByPriceSmart(less,greater);
    }

    private List<AdvertEntity> getAllByOneOrMoreParams(Boolean isTitle, Boolean isType, Boolean isLocation,
                                                       String title, String location, Long typeId,
                                                       BigDecimal minPrice, BigDecimal maxPrice) {
        if (isTitle && isType && isLocation) {
            return advertRepository.findAllByFullSearch(
                    title, typeId, location, minPrice, maxPrice);
        } else if (!isTitle && !isType && isLocation) {
            return advertRepository.findAllByLocation(location, minPrice, maxPrice);
        } else if (isTitle && !isType && !isLocation) {
            return advertRepository.findAllByTitle(title, minPrice, maxPrice);
        } else if (!isTitle && isType && !isLocation) {
            return advertRepository.findAllByType(typeId);

        } else if (!isTitle && isType) {
            return advertRepository.findAllByTypeAndLocation(
                    typeId, location, minPrice, maxPrice);
        } else if (isTitle && isType) {
            return advertRepository.findAllByTypeAndTitle(
                    typeId, title, minPrice, maxPrice);
        } else{
            return advertRepository.findAllByTitleAndLocation(
                    title, location, minPrice, maxPrice);
        }
    }

    private Boolean isAllNone(String title, String type, String location){
        return title.equals("none") && type.equals("none") && location.equals("none");
    }

    private AdvertSearchRequest getSearchRequest(HttpServletRequest request){
        String jsonBody = jsonUtil.getJson(request);
        return jsonUtil.getGson()
                .fromJson(jsonBody, AdvertSearchRequest.class);
    }

    private AdvertTypeEntity findByType(String type){
        return advertTypeRepository.findByType(type);
    }

}
