package ru.danis0n.avitoclone.service.advert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.advert.Available;
import ru.danis0n.avitoclone.dto.advert.Advert;
import ru.danis0n.avitoclone.dto.type.CustomType;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;
import ru.danis0n.avitoclone.entity.type.FullTypeEntity;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.repository.LocationRepository;
import ru.danis0n.avitoclone.repository.advert.AdvertRepository;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.service.image.ImageService;
import ru.danis0n.avitoclone.service.type.TypeService;
import ru.danis0n.avitoclone.util.JwtUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;
import ru.danis0n.avitoclone.util.search.DeepSearchCheckBoxHandler;
import ru.danis0n.avitoclone.util.search.SearchUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdvertServiceImpl implements AdvertService{

    private final AppUserService appUserService;
    private final ImageService imageService;
    private final JwtUtil jwtUtil;
    private final ObjectMapperUtil mapperUtil;
    private final SearchUtil searchUtil;
    private final AdvertRepository advertRepository;
    private final LocationRepository locationRepository;
    private final DeepSearchCheckBoxHandler checkBoxHandler;
    private final TypeService typeService;


    @Override
    public String create(HttpServletRequest request,
                         String title, String location,
                         String description, BigDecimal price,
                         MultipartFile[] files,
                         String mainType, String brandType,
                         String titleType, String subType) {
        try{
            String username = getUsernameFromRequest(request);

            AdvertEntity advert = new AdvertEntity();
            advert.setUser(getAppUserEntityByUsername(username));
            buildAdvert(advert,title,location,description,price,files, mainType, brandType,titleType, subType);
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
                         MultipartFile[] files) {

        AdvertEntity advert = findAdvertById(id);

        if(advert == null){
            return "Advert with current id was not found";
        }

        if (!advert.getUser().equals(
                getAppUserEntityByUsername(
                        getUsernameFromRequest(request)
            ))){
            return "You don't have permission to do it";
        }

        buildAdvert(advert,title,location,description,price,files);
        saveAdvert(advert);
        return "Advert has been updated";
    }

    @Override
    public String deleteById(HttpServletRequest request,Long id) {
        AdvertEntity advert = findAdvertById(id);

        if(advert == null){
            return "Advert with this id was not found";
        }

        if (!advert.getUser().equals(
                getAppUserEntityByUsername(
                        getUsernameFromRequest(request)
                ))){
            return "You don't have permission to do it";
        }
        deleteAdvert(advert);
        return "Advert has been deleted";
    }

    @Override
    public Advert getById(Long id) {
        AdvertEntity advert = findAdvertById(id);
        if(advert == null) return null;
        return mapToAdvert(advert);
    }

    @Override
    public List<Advert> getAll() {
        return mapToListOfAdverts(findAllAdvertEntities());
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
    public List<Advert> getAllByUser(Long id) {
        List<AdvertEntity> advertsByUser = findAllAdvertEntitiesByUserId(id);
        return mapToListOfAdverts(advertsByUser);
    }

    @Override
    public List<Advert> getAllByUserUnPower(Long id) {
        List<AdvertEntity> advertsByUser = findAllAdvertEntitiesByUserIdUnPower(id);
        return mapToListOfAdverts(advertsByUser);
    }

    @Override
    public List<Advert> getByParams(HttpServletRequest request) {
        return mapToListOfAdverts(searchUtil.getByParams(request));
    }

    @Override
    public List<Available> getAvailableQuantityBrand(HttpServletRequest request) {
        return searchUtil.getAvailableQuantityBrand(request);
    }

    @Override
    public List<Available> getAvailableQuantitySub(HttpServletRequest request) {
        return searchUtil.getAvailableQuantitySub(request);
    }

    @Override
    public List<Available> getAvailableQuantityMain(HttpServletRequest request) {
        return searchUtil.getAvailableQuantityMain(request);
    }

    @Override
    public List<Available> getAvailableQuantityLocation(HttpServletRequest request) {
        return searchUtil.getAvailableQuantityLocation(request);
    }

    @Override
    public String hideUserAdvertByUserId(Long userId, Long advertId, HttpServletRequest request) {
        AppUserEntity user = appUserService.getAppUserEntityById(userId);
        if(findAdvertById(advertId) == null)
            return "Advert with this id doesn't exist";
        if (!user.getUsername().equals(getUsernameFromRequest(request)))
            return "You don't have permissions";

        advertRepository.hideAdvertByUserId(true,userId,advertId);
        return null;
    }

    @Override
    public String unHideUserAdvertByUserId(Long userId, Long advertId, HttpServletRequest request) {
        AppUserEntity user = appUserService.getAppUserEntityById(userId);
        if(findAdvertById(advertId) == null)
            return "Advert with this id doesn't exist";
        if (!user.getUsername().equals(getUsernameFromRequest(request)))
            return "You don't have permissions";

        advertRepository.hideAdvertByUserId(false,userId,advertId);
        return null;
    }

    @Override
    public String hideAllUserAdvertsByUserId(Long userId, HttpServletRequest request) {
        AppUserEntity user = appUserService.getAppUserEntityById(userId);
        if (!user.getUsername().equals(getUsernameFromRequest(request)))
            return "You don't have permissions";

        advertRepository.hideAllAdvertsByUserId(true,userId);
        return null;
    }

    @Override
    public String unHideAllUserAdvertsByUserId(Long userId, HttpServletRequest request) {
        AppUserEntity user = appUserService.getAppUserEntityById(userId);
        if (!user.getUsername().equals(getUsernameFromRequest(request)))
            return "You don't have permissions";

        advertRepository.hideAllAdvertsByUserId(false, userId);
        return null;
    }

    @Override
    public List<CustomType> getBrandTypeByTitleType(String id) {
        return checkBoxHandler.getBrandTypeByTitleType(id);
    }

    @Override
    public List<CustomType> getSubTypeByTitleType(String id) {
        return checkBoxHandler.getSubTypeByTitleType(id);
    }

    @Override
    public List<CustomType> getMainTypeByTitleType(String id) {
        return checkBoxHandler.getMainTypeByTitleType(id);
    }

    @Override
    public List<CustomType> getLocations() {
        return checkBoxHandler.getLocations();
    }

    private List<Advert> mapToListOfAdverts(List<AdvertEntity> advertEntities){
        return mapperUtil.mapListToAdverts(advertEntities);
    }

    private void deleteAdvert(AdvertEntity advert){
        advertRepository.delete(advert);
    }

    private void buildAdvert(AdvertEntity advert,
                             String title, String location,
                             String description, BigDecimal price,
                             MultipartFile[] files){
        advert.setTitle(title);
        advert.setLocation(locationRepository.findByName(location));
        advert.setDescription(description);
        advert.setPrice(price);

        imageService.clearImageListByAd(advert);
        advert.clearList();
        buildImagesForAdvert(advert,files);
    }

    private void buildAdvert(AdvertEntity advert, String title, String location, String description,
                             BigDecimal price, MultipartFile[] files, String mainType, String brandType,
                             String titleType, String subType) {

        FullTypeEntity type = typeService.handleNewTypeForAdvert(mainType,brandType,titleType,subType);

        advert.setTitle(title);
        advert.setLocation(locationRepository.findByName(location));
        advert.setDescription(description);
        advert.setPrice(price);
        advert.setType(type);
        advert.setIsHidden(false);
        advert.setIsHiddenByAdmin(false);

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

    private AppUserEntity getAppUserEntityByUsername(String username){
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

    private List<AdvertEntity> findAllAdvertEntities(){
        return advertRepository.findAllUnHidden();
    }

    private List<AdvertEntity> findAllAdvertEntitiesByUserId(Long id){
        return advertRepository.findAllByUserId(id);
    }

    private List<AdvertEntity> findAllAdvertEntitiesByUserIdUnPower(Long id){
        return advertRepository.findAllByUserIdInPower(id);
    }

}
