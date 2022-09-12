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
                String username = jwtUtil.getUsernameFromToken(token);
                log.info("New advert was created by {}",username);

                AdvertEntity advert = new AdvertEntity();
                advert.setUser(appUserService.getAppUserEntity(username));
                buildAdvert(advert,title,location,description,price,files,type);
                advertRepository.save(advert);

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

        String username = jwtUtil.getUsernameFromRequest(request);
        AdvertEntity advert = advertRepository.findById(id).orElse(null);

        if(advert == null){
            return "null";
        }

        if (!advert.getUser().equals(appUserService.getAppUserEntity(username))){
            return "You don't have permission for it";
        }

        buildAdvert(advert,title,location,description,price,files,type);
        advertRepository.save(advert);
        return "Successful";
    }

    @Override
    public String deleteById(HttpServletRequest request,Long id) {

        String username = jwtUtil.getUsernameFromRequest(request);
        AdvertEntity advert = advertRepository.findById(id).orElse(null);

        if(advert == null){
            return "null";
        }

        if (!advert.getUser().equals(appUserService.getAppUserEntity(username))){
            return "You don't have permission for it";
        }

        advertRepository.delete(advert);
        return "Successful";
    }

    private void buildAdvert(AdvertEntity advert,
                             String title, String location,
                             String description, String price,
                             MultipartFile[] files, String type){
        advert.setTitle(title);
        advert.setLocation(location);
        advert.setDescription(description);
        advert.setPrice(price);
        advert.setType(advertTypeRepository.findByType(type));

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

    @Override
    public Advert getById(Long id) {
        AdvertEntity advert = advertRepository.findById(id).get();
        System.out.println("advert = " + advert);
        return mapperUtil.mapToAdvert(advert);
    }

    @Override
    public List<Advert> getAllByType(String type) {
        AdvertTypeEntity typeEntity = advertTypeRepository.findByType(type);
        List<AdvertEntity> advertsByType = advertRepository.findAllByType(typeEntity);

        List<Advert> adverts = new ArrayList<>();

        for (AdvertEntity advert : advertsByType){
            adverts.add(mapperUtil.mapToAdvert(advert));
        }
        return adverts;
    }

    @Override
    public List<Advert> getAll() {
        List<Advert> adverts = new ArrayList<>();
        for (AdvertEntity advert : advertRepository.findAll()){
            adverts.add(mapperUtil.mapToAdvert(advert));
        }
        return adverts;
    }

    @Override
    public List<Advert> getAllByUser(String username) {
        AppUserEntity user = appUserService.getAppUserEntity(username);
        List<AdvertEntity> advertsByUser = advertRepository.findAllByUser(user);
        List<Advert> adverts = new ArrayList<>();

        for (AdvertEntity advert : advertsByUser){
            adverts.add(mapperUtil.mapToAdvert(advert));
        }
        return adverts;
    }


    @Override
    public void createType(AdvertType advertType) {
        AdvertTypeEntity type = new AdvertTypeEntity();
        type.setType(advertType.getName());
        advertTypeRepository.save(type);
    }

    // TODO : FIX THIS!

    @Override
    public void addTypeToAdvert(String type, Long id) {
        AdvertTypeEntity advertType = advertTypeRepository.findByType(type);
        AdvertEntity advert = advertRepository.findById(id).get();
        advert.setType(advertType);
    }

    @Override
    public void removeTypeFromAdvert(String type, Long id) {
    }
}
