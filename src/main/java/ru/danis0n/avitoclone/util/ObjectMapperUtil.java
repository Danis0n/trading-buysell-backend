package ru.danis0n.avitoclone.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.*;
import ru.danis0n.avitoclone.entity.*;
import ru.danis0n.avitoclone.service.image.ImageService;

@Component
@RequiredArgsConstructor
public class ObjectMapperUtil {

    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    public Role mapToRole(RoleEntity entity){
        Role role = new Role();
        role.setId(entity.getId());
        role.setName(entity.getName());
        return role;
    }

    public AppUser mapToAppUserWithParams(AppUserEntity entity){
        AppUser user = mapToAppUser(entity);

        entity.getRoles().forEach(e ->{
            user.addRoleToAppUser(mapToRole(e));
        });

        entity.getAdverts().forEach(e -> {
            user.addAdvertToAppUser(mapToAdvertForList(e));
            System.out.println(e.getId());
        });
        return user;
    }

    public AppUser mapToAppUser(AppUserEntity entity){
        AppUser user = new AppUser();
        AppUserInfo info = new AppUserInfo();

        user.setId(entity.getId());
        user.setUsername(entity.getUsername());

        info.setName(entity.getUserInfo().getName());
        info.setEmail(entity.getUserInfo().getEmail());
        info.setPhoneNumber(entity.getUserInfo().getPhoneNumber());
        info.setRating(entity.getUserInfo().getRating());

        user.setInfo(info);
        return user;
    }

    public AppUserEntity mapToAppUserEntity(AppUser user){
        AppUserEntity entity = new AppUserEntity();

        entity.setUsername(user.getUsername());
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        entity.setEnabled(false);
        entity.setLocked(false);

        AppUserInfoEntity info = new AppUserInfoEntity();
        info.setName(user.getInfo().getName());
        info.setEmail(user.getInfo().getEmail());
        info.setPhoneNumber(user.getInfo().getPhoneNumber());

        entity.setUserInfo(info);
        return entity;
    }

    public Advert mapToAdvertForList(AdvertEntity entity) {
        Advert ad = new Advert();

        // no user
        ad.setId(entity.getId());
        ad.setType(new AdvertType(entity.getType().getId(),entity.getType().getType()));
        ad.setLocation(entity.getLocation());
        ad.setTitle(entity.getTitle());
        ad.setPrice(entity.getPrice());
        ad.setDescription(entity.getDescription());
        ad.setDateOfCreation(entity.getDateOfCreation());

        for(ImageEntity image : entity.getImages()){
            ad.addImageToAdvert(imageService.mapToImage(image));
        }
        return ad;
    }

    public Advert mapToAdvert(AdvertEntity entity) {
        Advert ad = new Advert();

        ad.setUser(mapToAppUser(entity.getUser()));
        ad.setId(entity.getId());
        ad.setType(new AdvertType(entity.getType().getId(),entity.getType().getType()));
        ad.setLocation(entity.getLocation());
        ad.setTitle(entity.getTitle());
        ad.setPrice(entity.getPrice());
        ad.setDescription(entity.getDescription());
        ad.setDateOfCreation(entity.getDateOfCreation());

        for(ImageEntity image : entity.getImages()){
            ad.addImageToAdvert(imageService.mapToImage(image));
        }
        return ad;
    }

}
