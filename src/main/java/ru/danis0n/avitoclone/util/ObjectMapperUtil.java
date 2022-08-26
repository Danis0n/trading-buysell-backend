package ru.danis0n.avitoclone.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.*;
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
        AppUser user = new AppUser();
        AppUserInfo info = new AppUserInfo();

        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setLocked(userEntity.isLocked());
        user.setEnabled(userEntity.isEnabled());
        user.setPassword("empty");

        info.setName(userEntity.getUserInfo().getName());
        info.setEmail(userEntity.getUserInfo().getEmail());
        info.setPhoneNumber(userEntity.getUserInfo().getPhoneNumber());
        info.setRating(userEntity.getUserInfo().getRating());
        info.setDateOfCreation(userEntity.getUserInfo().getDateOfCreation());

        user.setInfo(info);
        return user;
    }

    public AppUserEntity mapToNewAppUserEntity(AppUser user){
        AppUserEntity userEntity = new AppUserEntity();

        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setEnabled(false);
        userEntity.setLocked(false);

        AppUserInfoEntity info = new AppUserInfoEntity();
        info.setName(user.getInfo().getName());
        info.setEmail(user.getInfo().getEmail());
        info.setPhoneNumber(user.getInfo().getPhoneNumber());
        info.setDateOfCreation(user.getInfo().getDateOfCreation());

        userEntity.setUserInfo(info);
        return userEntity;
    }

    public Advert mapToAdvert(AdvertEntity advertEntity) {
        Advert advert = new Advert();

        advert.setUserId(advertEntity.getUser().getId());
        advert.setId(advertEntity.getId());
        advert.setType(new AdvertType(advertEntity.getType().getType()));
        advert.setLocation(advertEntity.getLocation());
        advert.setTitle(advertEntity.getTitle());
        advert.setPrice(advertEntity.getPrice());
        advert.setDescription(advertEntity.getDescription());
        advert.setDateOfCreation(advertEntity.getDateOfCreation());

        for(ImageEntity image : advertEntity.getImages()){
            advert.addImageToAdvert(imageService.mapToImage(image));
        }
        return advert;
    }

    public Comment mapToComment(CommentEntity commentEntity){
        Comment comment = new Comment();

        comment.setCreatedBy(commentEntity.getOwnerUser().getUsername());
        comment.setTo(commentEntity.getUser().getUsername());
        comment.setId(commentEntity.getId());
        comment.setAdvertName(commentEntity.getAdvertName());
        comment.setRating(commentEntity.getRating());
        comment.setTitle(commentEntity.getTitle());
        comment.setDescription(commentEntity.getDescription());
        return comment;
    }

    public CommentEntity mapToCommentEntity(CommentRequest commentRequest){
        CommentEntity comment = new CommentEntity();

        comment.setUser(appUserRepository.findByUsername(commentRequest.getTo()));
        comment.setOwnerUser(appUserRepository.findByUsername(commentRequest.getCreatedBy()));
        comment.setTitle(commentRequest.getTitle());
        comment.setDescription(commentRequest.getDescription());
        comment.setAdvertName(commentRequest.getAdvertName());
        comment.setRating(commentRequest.getRating());
        return comment;
    }
}
