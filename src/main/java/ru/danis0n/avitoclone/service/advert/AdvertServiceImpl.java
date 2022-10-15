package ru.danis0n.avitoclone.service.advert;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.advert.Advert;
import ru.danis0n.avitoclone.dto.advert.AdvertSearchRequest;
import ru.danis0n.avitoclone.dto.advert.AdvertType;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;
import ru.danis0n.avitoclone.entity.advert.AdvertTypeEntity;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.repository.AdvertRepository;
import ru.danis0n.avitoclone.repository.AdvertTypeRepository;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.service.image.ImageService;
import ru.danis0n.avitoclone.util.JsonUtil;
import ru.danis0n.avitoclone.util.JwtUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdvertServiceImpl implements AdvertService{

    private final AppUserService appUserService;
    private final ImageService imageService;
    private final JwtUtil jwtUtil;
    private final JsonUtil jsonUtil;
    private final ObjectMapperUtil mapperUtil;
    private final AdvertRepository advertRepository;
    private final AdvertTypeRepository advertTypeRepository;

    @Override
    public String create(HttpServletRequest request,
                         String title, String location,
                         String description, BigDecimal price,
                         MultipartFile[] files, String type) {
        try{
            String username = getUsernameFromRequest(request);

            AdvertEntity advert = new AdvertEntity();
            advert.setUser(getAppUserEntity(username));
            buildAdvert(advert,title,location,description,price,files,type);
            saveAdvert(advert);
            log.info("New advert was created by {}",username);

            return "New advert was created";
        }catch (Exception e){
            return "Exception while creating";
        }
    }

    @Override
    public String update(HttpServletRequest request, Long id,
                         String title, String location,
                         String description, BigDecimal price,
                         MultipartFile[] files, String type) {

        String username = getUsernameFromRequest(request);
        AdvertEntity advert = findAdvertById(id);

        if(advert == null){
            return "Advert with this id was not found";
        }

        if (!advert.getUser().equals(getAppUserEntity(username))){
            return "You don't have permission to do it";
        }

        buildAdvert(advert,title,location,description,price,files,type);
        saveAdvert(advert);
        return "Advert has been updated";
    }

    @Override
    public String deleteById(HttpServletRequest request,Long id) {
        String username = getUsernameFromRequest(request);
        AdvertEntity advert = findAdvertById(id);

        if(advert == null){
            return "Advert with this id was not found";
        }

        if (!advert.getUser().equals(getAppUserEntity(username))){
            return "You don't have permission for it";
        }

        deleteAdvertById(advert);
        return "Advert has been deleted";
    }

    @Override
    public Advert getById(Long id) {
        AdvertEntity advert = findAdvertById(id);
        if(advert == null) return null;
        return mapToAdvert(advert);
    }

    @Override
    public List<Advert> getAllByType(String type) {
        AdvertTypeEntity typeEntity = findByType(type);
        List<AdvertEntity> advertsByType = findAllAdvertEntitiesByType(typeEntity);

        List<Advert> adverts = new ArrayList<>();

        for (AdvertEntity advert : advertsByType){
            adverts.add(mapToAdvert(advert));
        }
        return adverts;
    }

    @Override
    public List<Advert> getAll() {
        List<Advert> adverts = new ArrayList<>();
        for (AdvertEntity advert : findAllAdvertEntities()){
            adverts.add(mapToAdvert(advert));
        }
        return adverts;
    }

    @Override
    public List<Advert> getLatest(Long quantity) {
        return mapToListOfAdverts(advertRepository.findAllLatest(quantity));
    }

    @Override
    public List<Advert> getExamples() {
        return mapToListOfAdverts(advertRepository.findExamples());
    }

    @Override
    public List<Advert> getAllByUser(String username) {
        AppUserEntity user = getAppUserEntity(username);
        List<AdvertEntity> advertsByUser = findAllAdvertEntitiesByUser(user);
        List<Advert> adverts = new ArrayList<>();

        for (AdvertEntity advert : advertsByUser){
            adverts.add(mapToAdvert(advert));
        }
        return adverts;
    }

    @Override
    public List<Advert> getByParams(HttpServletRequest request) {
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
            else return mapToListOfAdverts(getByPrice(minPrice,maxPrice));
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

    @Override
    public void createType(AdvertType advertType) {
        AdvertTypeEntity type = new AdvertTypeEntity();
        type.setType(advertType.getName());
        saveType(type);
    }

    @Override
    public void addTypeToAdvert(String type, Long id) {
        AdvertTypeEntity advertType = findByType(type);
        AdvertEntity advert = findAdvertById(id);
        if(advert == null){
            return;
        }
        advert.setType(advertType);
    }

    @Override
    public void removeTypeFromAdvert(String type, Long id) {
    }

    private List<Advert> getAllByOneOrMoreParams(Boolean isTitle, Boolean isType, Boolean isLocation,
                                                 String title, String location, Long typeId,
                                                 BigDecimal minPrice, BigDecimal maxPrice) {
        if (isTitle && isType && isLocation) {
            return mapToListOfAdverts(
                    advertRepository.findAllByFullSearch(
                            title, typeId, location, minPrice, maxPrice));
        } else if (!isTitle && !isType && isLocation) {
            return mapToListOfAdverts(
                    advertRepository.findAllByLocation(location, minPrice, maxPrice));
        } else if (isTitle && !isType && !isLocation) {
            return mapToListOfAdverts(
                    advertRepository.findAllByTitle(title, minPrice, maxPrice));
        } else if (!isTitle && isType && !isLocation) {
            return mapToListOfAdverts(
                    advertRepository.findAllByType(typeId));

        } else if (!isTitle && isType) {
            return mapToListOfAdverts(
                    advertRepository.findAllByTypeAndLocation(
                            typeId, location, minPrice, maxPrice));
        } else if (isTitle && isType) {
            return mapToListOfAdverts(
                    advertRepository.findAllByTypeAndTitle(
                            typeId, title, minPrice, maxPrice));
        } else{
            return mapToListOfAdverts(
                    advertRepository.findAllByTitleAndLocation(
                            title, location, minPrice, maxPrice));
        }
    }

    private Boolean isAllNone(String title, String type, String location){
        return title.equals("none") && type.equals("none") && location.equals("none");
    }

    private List<AdvertEntity> getByPrice(BigDecimal less, BigDecimal greater){
        return advertRepository.findAllByPriceSmart(less,greater);
    }

    private AdvertSearchRequest getSearchRequest(HttpServletRequest request){
        String jsonBody = jsonUtil.getJson(request);
        return jsonUtil.getGson()
                .fromJson(jsonBody, AdvertSearchRequest.class);
    }

    private List<Advert> mapToListOfAdverts(List<AdvertEntity> advertEntities){
        return mapperUtil.mapListToAdverts(advertEntities);
    }

    private List<AdvertEntity> findAllAdvertEntitiesByType(AdvertTypeEntity type){
        return advertRepository.findAllByType(type.getId());
    }

    private void deleteAdvertById(AdvertEntity advert){
        advertRepository.delete(advert);
    }

    private void buildAdvert(AdvertEntity advert,
                             String title, String location,
                             String description, BigDecimal price,
                             MultipartFile[] files, String type){
        advert.setTitle(title);
        advert.setLocation(location);
        advert.setDescription(description);
        advert.setPrice(price);
        advert.setType(findByType(type));

        imageService.clearImageListByAd(advert);
        advert.clearList();
        buildImagesForAdvert(advert,files);
    }

    private void buildImagesForAdvert(AdvertEntity advert, MultipartFile[] files) {
        for(MultipartFile file : files){
            try{
                advert.addImageToAd(imageService.saveFile(file,advert));
            }catch (IOException e){
                System.out.println("Exception here: AD-SERVICE= " + e);
            }
        }
    }

    private String getUsernameFromToken(String token){
        return jwtUtil.getUsernameFromToken(token);
    }

    private AppUserEntity getAppUserEntity(String username){
        return appUserService.getAppUserEntityByUsername(username);
    }

    private void saveAdvert(AdvertEntity advert){
        advertRepository.save(advert);
    }

    private String getUsernameFromRequest(HttpServletRequest request){
        return jwtUtil.getUsernameFromRequest(request);
    }

    private AdvertEntity findAdvertById(Long id){
        return advertRepository.findById(id).orElse(null);
    }

    private Advert mapToAdvert(AdvertEntity advert){
        return mapperUtil.mapToAdvert(advert);
    }

    private void saveType(AdvertTypeEntity type){
        advertTypeRepository.save(type);
    }

    private AdvertTypeEntity findByType(String type){
        return advertTypeRepository.findByType(type);
    }

    private List<AdvertEntity> findAllAdvertEntities(){
        return advertRepository.findAll();
    }

    private List<AdvertEntity> findAllAdvertEntitiesByUser(AppUserEntity user){
        return advertRepository.findAllByUser(user);
    }

}
