package ru.danis0n.avitoclone.service.advert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.Advert;
import ru.danis0n.avitoclone.dto.AdvertType;
import ru.danis0n.avitoclone.entity.AdvertEntity;
import ru.danis0n.avitoclone.entity.AdvertTypeEntity;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.repository.AdvertRepository;
import ru.danis0n.avitoclone.repository.AdvertTypeRepository;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.service.image.ImageService;
import ru.danis0n.avitoclone.util.JwtUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    private final ObjectMapperUtil mapperUtil;
    private final AdvertRepository advertRepository;
    private final AdvertTypeRepository advertTypeRepository;

    // TODO : REFACTOR IT !

    @Override
    public String create(HttpServletRequest request,
                         String title, String location,
                         String description, String price,
                         MultipartFile[] files, String type) {

        String tokenFromRequest = request.getHeader(AUTHORIZATION);
        if(tokenFromRequest != null && tokenFromRequest.startsWith("Bearer ")){
            try{
                String token = tokenFromRequest.substring("Bearer ".length());
                String username = getUsernameFromToken(token);

                log.info("New advert was created by {}",username);

                AdvertEntity advert = new AdvertEntity();
                advert.setUser(getAppUserEntity(username));
                buildAdvert(advert,title,location,description,price,files,type);
                saveAdvert(advert);

                return "Successful";
            }catch (Exception e){
                return "Exception while creating";
            }
        }
        return "Error!";
    }

    @Override
    public String update(HttpServletRequest request, Long id,
                         String title, String location,
                         String description, String price,
                         MultipartFile[] files, String type) {

        String username = getUsernameFromRequest(request);
        AdvertEntity advert = findAdvertById(id);

        if(advert == null){
            return "null";
        }

        if (!advert.getUser().equals(getAppUserEntity(username))){
            return "You don't have permission for it";
        }

        buildAdvert(advert,title,location,description,price,files,type);
        saveAdvert(advert);
        return "Successful";
    }

    @Override
    public String deleteById(HttpServletRequest request,Long id) {

        String username = getUsernameFromRequest(request);
        AdvertEntity advert = findAdvertById(id);

        if(advert == null){
            return "null";
        }

        if (!advert.getUser().equals(getAppUserEntity(username))){
            return "You don't have permission for it";
        }

        deleteAdvertById(advert);
        return "Successful";
    }

    @Override
    public Advert getById(Long id) {
        AdvertEntity advert = findAdvertById(id);
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

    private List<AdvertEntity> findAllAdvertEntitiesByType(AdvertTypeEntity type){
        return advertRepository.findAllByType(type);
    }

    private void deleteAdvertById(AdvertEntity advert){
        advertRepository.delete(advert);
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
    public void createType(AdvertType advertType) {
        AdvertTypeEntity type = new AdvertTypeEntity();
        type.setType(advertType.getName());
        saveType(type);
    }

    // TODO : FIX THIS!

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

    private void buildAdvert(AdvertEntity advert,
                             String title, String location,
                             String description, String price,
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
