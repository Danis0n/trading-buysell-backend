package ru.danis0n.avitoclone.service.advert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.advert.Advert;
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
import ru.danis0n.avitoclone.util.SearchUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private final SearchUtil searchUtil;
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
            advert.setUser(getAppUserEntityByUsername(username));
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

        buildAdvert(advert,title,location,description,price,files,type);
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
    public List<Advert> getAllByType(String type) {
        Long typeId = findByType(type).getId();
        List<AdvertEntity> advertsByType = findAllAdvertEntitiesByType(typeId);

        List<Advert> adverts = new ArrayList<>();
        for (AdvertEntity advert : advertsByType){
            adverts.add(mapToAdvert(advert));
        }
        return adverts;
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
    public List<Advert> getByParams(HttpServletRequest request) {
        return mapToListOfAdverts(searchUtil.getByParams(request));
    }

    @Override
    public void createType(AdvertType advertType) {
        AdvertTypeEntity type = new AdvertTypeEntity();
        type.setType(advertType.getName());
        saveType(type);
    }

    private List<Advert> mapToListOfAdverts(List<AdvertEntity> advertEntities){
        return mapperUtil.mapListToAdverts(advertEntities);
    }

    private List<AdvertEntity> findAllAdvertEntitiesByType(Long typeId){
        return advertRepository.findAllByType(typeId);
    }

    private void deleteAdvert(AdvertEntity advert){
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

    private void saveType(AdvertTypeEntity type){
        advertTypeRepository.save(type);
    }

    private AdvertTypeEntity findByType(String type){
        return advertTypeRepository.findByType(type);
    }

    private List<AdvertEntity> findAllAdvertEntities(){
        return advertRepository.findAll();
    }

    private List<AdvertEntity> findAllAdvertEntitiesByUserId(Long id){
        return advertRepository.findAllByUserId(id);
    }

}
