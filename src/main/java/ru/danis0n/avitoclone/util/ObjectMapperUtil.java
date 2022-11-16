package ru.danis0n.avitoclone.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.advert.Advert;
import ru.danis0n.avitoclone.dto.advert.Available;
import ru.danis0n.avitoclone.dto.advert.Location;
import ru.danis0n.avitoclone.dto.appuser.AppUser;
import ru.danis0n.avitoclone.dto.appuser.AppUserInfo;
import ru.danis0n.avitoclone.dto.appuser.RegistrationRequest;
import ru.danis0n.avitoclone.dto.appuser.Role;
import ru.danis0n.avitoclone.dto.comment.Comment;
import ru.danis0n.avitoclone.dto.comment.CommentRequest;
import ru.danis0n.avitoclone.dto.notification.Notification;
import ru.danis0n.avitoclone.dto.type.*;
import ru.danis0n.avitoclone.entity.advert.*;
import ru.danis0n.avitoclone.entity.notification.NotificationEntity;
import ru.danis0n.avitoclone.entity.type.*;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.entity.user.AppUserInfoEntity;
import ru.danis0n.avitoclone.entity.user.RoleEntity;
import ru.danis0n.avitoclone.repository.LocationRepository;
import ru.danis0n.avitoclone.repository.type.BrandTypeRepository;
import ru.danis0n.avitoclone.repository.type.MainTypeRepository;
import ru.danis0n.avitoclone.repository.type.SubTypeRepository;
import ru.danis0n.avitoclone.repository.user.AppUserRepository;
import ru.danis0n.avitoclone.service.image.ImageService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ObjectMapperUtil {

    private final ImageService imageService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final BrandTypeRepository brandTypeRepository;
    private final SubTypeRepository subTypeRepository;
    private final MainTypeRepository mainTypeRepository;
    private final LocationRepository locationRepository;

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
            user.addAdvertToAppUser(e.getId().toString());
        });
        return user;
    }

    private AppUser mapToAppUser(AppUserEntity userEntity){
        AppUserInfo info = AppUserInfo.builder().
                name(userEntity.getUserInfo().getName()).
                email(userEntity.getUserInfo().getEmail()).
                phone(userEntity.getUserInfo().getPhone()).
                rating(userEntity.getUserInfo().getRating().toString().substring(0,3)).
                dateOfCreation(userEntity.getUserInfo().getDateOfCreation().toString()).
                build();

        return AppUser.builder().
                id(userEntity.getId()).
                image(imageService.mapToImage(userEntity.getAvatar())).
                username(userEntity.getUsername()).
                locked(userEntity.isLocked()).
                enabled(userEntity.isEnabled()).
                info(info).
                roles(new ArrayList<>()).
                advertIds(new ArrayList<>()).
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
                adverts(new HashSet<>()).
                roles(new ArrayList<>()).
                build();
    }

    public List<Advert> mapListToAdverts(List<AdvertEntity> entities) {
        if(entities == null) return null;
        List<Advert> adverts = new ArrayList<>();
        for(AdvertEntity advert : entities){
            adverts.add(mapToAdvert(advert));
        }
        return adverts;
    }

    public FullType mapToFullType(FullTypeEntity fullType) {
        return FullType.builder().
                mainType(mapToMainType(fullType.getMainType())).
                brandType(mapToBrandType(fullType.getBrandType())).
                subType(mapToSubType(fullType.getSubType())).
                titleType(mapToTitleType(fullType.getTitleType())).
                build();
    }

    public BrandType mapToBrandType(BrandTypeEntity brandType) {
        return BrandType.builder().
                id(brandType.getId()).
                name(brandType.getName()).
                build();
    }

    public TitleType mapToTitleType(TitleTypeEntity titleType) {
        return TitleType.builder().
                id(titleType.getId()).
                name(titleType.getName()).
                build();
    }

    public SubType mapToSubType(SubTypeEntity subType) {
        return SubType.builder().
                id(subType.getId()).
                name(subType.getName()).
                build();
    }

    public MainType mapToMainType(MainTypeEntity mainType) {
        return MainType.builder().
                id(mainType.getId()).
                name(mainType.getName()).
                build();
    }

    public Location mapToLocation(LocationEntity locationEntity) {
        return Location.builder().
                description(locationEntity.getDescription()).
                name(locationEntity.getName()).
                build();
    }

    public Advert mapToAdvert(AdvertEntity advertEntity) {
        Advert advert = Advert.builder().
                id(advertEntity.getId()).
                userId(advertEntity.getUser().getId()).
                type(mapToFullType(advertEntity.getType())).
                location(mapToLocation(advertEntity.getLocation())).
                title(advertEntity.getTitle()).
                isHidden(advertEntity.getIsHidden()).
                isHiddenByAdmin(advertEntity.getIsHiddenByAdmin()).
                price(advertEntity.getPrice()).
                description(advertEntity.getDescription()).
                dateOfCreation(advertEntity.getDateOfCreation().toString()).
                images(new ArrayList<>()).
                build();

        for(ImageEntity image : advertEntity.getImages()){
            advert.addImageToAdvert(imageService.mapToImage(image));
        }
        return advert;
    }

    public List<Comment> mapListToComments(List<CommentEntity> commentEntities) {
        List<Comment> comments = new ArrayList<>();
        for(CommentEntity comment : commentEntities){
            comments.add(mapToComment(comment));
        }
        return comments;
    }

    public Comment mapToComment(CommentEntity commentEntity){
        return Comment.builder().
                createdBy(commentEntity.getCreatedBy().getId().toString()).
                to(commentEntity.getTo().getId().toString()).
                id(commentEntity.getId()).
                advertName(commentEntity.getAdvertName()).
                title(commentEntity.getTitle()).
                description(commentEntity.getDescription()).
                rating(commentEntity.getRating()).
                build();
    }

    public CommentEntity mapToCommentEntity(CommentRequest commentRequest){
        return CommentEntity.builder().
                to(appUserRepository.findById(commentRequest.getTo()).orElse(null)).
                createdBy(appUserRepository.findById(commentRequest.getCreatedBy()).orElse(null)).
                title(commentRequest.getTitle()).
                description(commentRequest.getDescription()).
                advertName(commentRequest.getAdvertName()).
                rating(commentRequest.getRating()).
                build();
    }

    public List<Available> mapToAvailableBrand(List<AdvertAvailable> availables) {
        List<Available> mappedList = new ArrayList<>();
        for (AdvertAvailable element : availables) {
            mappedList.add(
                    new Available(brandTypeRepository.getById(element.getId()).getName(),element.getQuantity())
            );
        }
        return mappedList;
    }

    public List<Available> mapToAvailableSub(List<AdvertAvailable> availables) {
        List<Available> mappedList = new ArrayList<>();
        for (AdvertAvailable element : availables) {
            mappedList.add(
                    new Available(subTypeRepository.getById(element.getId()).getName(),element.getQuantity())
            );
        }
        return mappedList;
    }

    public List<Available> mapToAvailableMain(List<AdvertAvailable> availables) {
        List<Available> mappedList = new ArrayList<>();
        for (AdvertAvailable element : availables) {
            mappedList.add(
                    new Available(mainTypeRepository.getById(element.getId()).getName(),element.getQuantity())
            );
        }
        return mappedList;
    }

    public List<Available> mapToAvailableLocation(List<AdvertAvailable> availables) {
        List<Available> mappedList = new ArrayList<>();
        for (AdvertAvailable element : availables) {
            mappedList.add(
                    new Available(locationRepository.getById(element.getId()).getName(),element.getQuantity())
            );
        }
        return mappedList;
    }

    public Notification mapToNotification(NotificationEntity notificationEntity) {
        return Notification.builder().
                id(notificationEntity.getId()).
                message(notificationEntity.getMessage()).
                seenByUser(notificationEntity.isSeenByUser()).
                dateOfCreation(String.valueOf(notificationEntity.getDateOfCreation())).
                userId(String.valueOf(notificationEntity.getUser().getId())).
                build();
    }

    public List<Notification> mapToListNotification(List<NotificationEntity> notificationEntities) {
        List<Notification> notifications = new ArrayList<>();

        for (NotificationEntity element : notificationEntities)
            notifications.add(mapToNotification(element));

        return notifications;
    }

    public List<CustomType> mapToListCustomType(List<CustomTypeEntity> customTypeEntities) {
        List<CustomType> customTypes = new ArrayList<>();
        for(CustomTypeEntity element : customTypeEntities)
            customTypes.add(mapToCustomType(element));
        return customTypes;
    }

    public CustomType mapToCustomType(CustomTypeEntity customTypeEntity) {
        return CustomType.builder().
                description(customTypeEntity.getDescription()).
                name(customTypeEntity.getName()).
                build();
    }

}