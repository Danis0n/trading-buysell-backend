package ru.danis0n.avitoclone.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.*;
import ru.danis0n.avitoclone.dto.appuser.AppUser;
import ru.danis0n.avitoclone.dto.appuser.AppUserInfo;
import ru.danis0n.avitoclone.entity.*;
import ru.danis0n.avitoclone.repository.AppUserRepository;
import ru.danis0n.avitoclone.service.image.ImageService;

@Component
@RequiredArgsConstructor
public class ObjectMapperUtil {

    private final ImageService imageService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public Role mapToRole(RoleEntity roleEntity){
        Role role = new Role();
        role.setName(roleEntity.getName());
        return role;
    }

    public AppUser mapToAppUserWithParams(AppUserEntity userEntity){
        AppUser user = mapToAppUser(userEntity);

        userEntity.getRoles().forEach(e ->{
            user.addRoleToAppUser(mapToRole(e));
        });

        userEntity.getAdverts().forEach(e -> {
            user.addAdvertToAppUser(mapToAdvert(e));
            System.out.println(e.getId());
        });
        return user;
    }

    public AppUser mapToAppUser(AppUserEntity userEntity){
        AppUserInfo info = AppUserInfo.builder().
                name(userEntity.getUserInfo().getName()).
                email(userEntity.getUserInfo().getEmail()).
                phone(userEntity.getUserInfo().getPhone()).
                rating(userEntity.getUserInfo().getRating()).
                dateOfCreation(userEntity.getUserInfo().getDateOfCreation()).
                build();

        return AppUser.builder().
                id(userEntity.getId()).
                username(userEntity.getUsername()).
                locked(userEntity.isLocked()).
                enabled(userEntity.isEnabled()).
                info(info).
                build();
    }

    public AppUserEntity mapToNewAppUserEntityFromRequest(RegistrationRequest userRequest){
        AppUserInfoEntity info = AppUserInfoEntity.builder().
                name(userRequest.getName()).
                email(userRequest.getEmail()).
                phone(userRequest.getPhone()).
                build();

        return AppUserEntity.builder().
                username(userRequest.getUsername()).
                password(passwordEncoder.encode(userRequest.getPassword())).
                enabled(false).
                locked(false).
                userInfo(info).
                build();
    }

    public Advert mapToAdvert(AdvertEntity advertEntity) {
        Advert advert = Advert.builder().
                userId(advertEntity.getUser().getId()).
                id(advertEntity.getId()).
                type(new AdvertType(advertEntity.getType().getType())).
                location(advertEntity.getLocation()).
                title(advertEntity.getTitle()).
                price(advertEntity.getPrice()).
                description(advertEntity.getDescription()).
                dateOfCreation(advertEntity.getDateOfCreation()).
                build();

        for(ImageEntity image : advertEntity.getImages()){
            advert.addImageToAdvert(imageService.mapToImage(image));
        }
        return advert;
    }

    public Comment mapToComment(CommentEntity commentEntity){

        return Comment.builder().
                createdBy(commentEntity.getCreatedBy().getUsername()).
                to(commentEntity.getTo().getUsername()).
                id(commentEntity.getId()).
                advertName(commentEntity.getAdvertName()).
                title(commentEntity.getTitle()).
                description(commentEntity.getDescription()).
                rating(commentEntity.getRating()).
                build();
    }

    public CommentEntity mapToCommentEntity(CommentRequest commentRequest){
        return CommentEntity.builder().
                to(appUserRepository.findByUsername(commentRequest.getTo())).
                createdBy(appUserRepository.findByUsername(commentRequest.getCreatedBy())).
                title(commentRequest.getTitle()).
                description(commentRequest.getDescription()).
                advertName(commentRequest.getAdvertName()).
                rating(commentRequest.getRating()).
                build();
    }
}
