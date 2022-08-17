package ru.danis0n.avitoclone.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.Advert;
import ru.danis0n.avitoclone.dto.AdvertType;
import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.entity.AdvertEntity;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.ImageEntity;
import ru.danis0n.avitoclone.entity.RoleEntity;
import ru.danis0n.avitoclone.service.image.ImageService;

@Component
@RequiredArgsConstructor
public class ObjectMapperUtil {

    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    public Advert mapToAdvert(AdvertEntity entity) {
        Advert ad = new Advert();

        ad.setId(entity.getId());
        ad.setType(new AdvertType(entity.getType().getId(),entity.getType().getType()));
        ad.setTitle(entity.getTitle());
        ad.setPrice(entity.getPrice());
        ad.setDescription(entity.getDescription());
        ad.setDateOfCreation(entity.getDateOfCreation());

        for(ImageEntity image : entity.getImages()){
            ad.addImageToAdvert(imageService.mapToImage(image));
        }
        return ad;
    }

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
            user.addAdvertToAppUser(mapToAdvert(e));
            System.out.println(e.getId());
        });
        return user;
    }

    public AppUser mapToAppUser(AppUserEntity entity){
        AppUser user = new AppUser();

        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setPhoneNumber(entity.getPhoneNumber());
        user.setEnabled(entity.isEnabled());
        user.setLocked(entity.isLocked());
        user.setDateOfCreated(entity.getDateOfCreated());

        return user;
    }

    public AppUserEntity mapToAppUserEntity(AppUser user){
        AppUserEntity entity = new AppUserEntity();

        entity.setName(user.getName());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        entity.setEnabled(false);
        entity.setLocked(false);

        return entity;
    }

}
