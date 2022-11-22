package ru.danis0n.avitoclone.service.appuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.advert.Image;
import ru.danis0n.avitoclone.dto.appuser.RegistrationRequest;
import ru.danis0n.avitoclone.dto.appuser.AppUser;
import ru.danis0n.avitoclone.dto.appuser.Role;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;
import ru.danis0n.avitoclone.entity.advert.ImageEntity;
import ru.danis0n.avitoclone.entity.token.ConfirmationToken;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;
import ru.danis0n.avitoclone.entity.user.RoleEntity;
import ru.danis0n.avitoclone.repository.user.AppUserInfoRepository;
import ru.danis0n.avitoclone.repository.user.AppUserRepository;
import ru.danis0n.avitoclone.repository.RoleRepository;
import ru.danis0n.avitoclone.service.confirm.ConfirmationTokenService;
import ru.danis0n.avitoclone.service.image.ImageService;
import ru.danis0n.avitoclone.util.JwtUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final AppUserInfoRepository appUserInfoRepository;
    private final ImageService imageService;
    private final RoleRepository roleRepository;
    private final ObjectMapperUtil mapperUtil;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserEntity user = getAppUserEntityByUsername(username);
        if(user == null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User {} found in the database",user.getUsername());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getName()))
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(),authorities
        );
    }

    @Override
    public String saveAppUser(RegistrationRequest userRequest) {
        AppUserEntity entity = mapToNewAppUserEntityFromRequest(userRequest);
        saveUser(entity);
        addRoleToAppUser(entity,"ROLE_NOT_CONFIRMED");

        if(userRequest.getFile() != null) {
            try {
                ImageEntity image = imageService.saveFile(userRequest.getFile(),null);
                entity.setAvatar(image);
                saveUser(entity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                entity
        );
        saveConfirmationToken(confirmationToken);
        return token;
    }

    @Override
    public String getUsernameFromRequest(HttpServletRequest request){
        return jwtUtil.getUsernameFromRequest(request);
    }

    @Override
    public String saveUserPassword(Long id, String newPassword, String oldPassword, HttpServletRequest request, HttpServletResponse response) {

        AppUserEntity user = getAppUserEntityById(id);
        if(user == null)
            return "User does not exist!";

        String username = getUsernameFromRequest(request);
        String DBPassword = user.getPassword();

        if(validateNotUser(user.getUsername(), username))
            return "You don't have enough permissions!";

        boolean isNotValid = validateEqualInputPasswords(newPassword,oldPassword,DBPassword) ||
                validateEqualOldPasswordFromDB(oldPassword,DBPassword);

        if(isNotValid || !newPassword.matches("^[A-Za-z0-9_-]{8,20}$"))
            return "Password error!";

        user.setPassword(encoder.encode(newPassword));
        saveUser(user);
        return "Success!";
    }

    @Override
    public String saveUserName(Long id, String name, HttpServletRequest request, HttpServletResponse response) {

        AppUserEntity user = getAppUserEntityById(id);
        if(user == null)
            return "User does not exist!";

        String username = getUsernameFromRequest(request);
        String currentUsername = user.getUsername();
        String currentName = user.getUserInfo().getName();

        if(validateNotUser(currentUsername, username))
            return "You don't have enough permissions!";

        if(name.equals(currentName))
            return "You have the same name already!";

        user.getUserInfo().setName(name);
        saveUser(user);
        return "Success!";
    }

    @Override
    public String saveUserPhone(Long id, String phone, HttpServletRequest request, HttpServletResponse response) {

        AppUserEntity user = getAppUserEntityById(id);
        if(user == null)
            return "User does not exist!";

        String username = getUsernameFromRequest(request);
        String currentUsername = user.getUsername();
        String currentPhone = user.getUserInfo().getPhone();

        if(validateNotUser(currentUsername, username))
            return "You don't have enough permissions!";

        if(phone.equals(currentPhone))
            return "You have the same phone already!";

        user.getUserInfo().setPhone(phone);
        saveUser(user);
        return "Success!";
    }

    @Override
    public String saveUserEmail(Long id, String email, HttpServletRequest request, HttpServletResponse response) {

        AppUserEntity user = getAppUserEntityById(id);
        if(user == null)
            return "User does not exist!";

        String username = getUsernameFromRequest(request);
        String currentEmail = user.getUserInfo().getEmail();
        String currentUsername = user.getUsername();

        if(validateNotUser(currentUsername, username))
            return "You don't have enough permissions!";

        if(email.equals(currentEmail))
            return "You have the same email already!";

        user.getUserInfo().setEmail(email);
        saveUser(user);
        return "Success!";
    }

    @Override
    public Boolean saveUserImage(Long id, MultipartFile file, HttpServletRequest request, HttpServletResponse response) {

        AppUserEntity user = getAppUserEntityById(id);

        String username = getUsernameFromRequest(request);
        if(!user.getUsername().equals(username)) {
            return false;
        }

        try {
            ImageEntity image = imageService.saveFile(file,null);
            user.setAvatar(image);
            saveUser(user);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public Boolean deleteUserImage(Long id, HttpServletRequest request, HttpServletResponse response) {
        AppUserEntity user = getAppUserEntityById(id);

        String username = getUsernameFromRequest(request);
        if(!user.getUsername().equals(username)) {
            return false;
        }
        user.setAvatar(null);
        saveUser(user);

        return true;
    }

    private boolean validateNotUser(String username1, String username2) {
        return !username1.equals(username2);
    }

    @Override
    public Role saveRole(Role role) {
        RoleEntity entity = new RoleEntity();
        entity.setName(role.getName());
        roleRepository.save(entity);
        return role;
    }

    @Override
    public AppUser getAppUser(String username) {
        AppUserEntity entity = getAppUserEntityByUsername(username);
        return mapToAppUserWithParams(entity);
    }

    @Override
    public AppUserEntity getAppUserEntityByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUserEntity getAppUserEntityById(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    @Override
    public List<AppUser> getAppUsers() {
        List<AppUserEntity> entities = findAllUsers();
        List<AppUser> users = new ArrayList<>();
        entities.forEach(e -> {
            users.add(mapToAppUserWithParams(e));
        });

        return users;
    }

    @Override
    public AppUser getAppUserById(Long id) {
        AppUserEntity user = appUserRepository.findById(id).orElse(null);
        if(user != null){
            return mapToAppUserWithParams(user);
        }
        return null;
    }

    @Override
    public void addRoleToAppUser(AppUserEntity user, String roleName) {
        RoleEntity role = findByName(roleName);
        user.addRoleToAppUser(role);
    }

    @Override
    public void removeRoleFromAppUser(AppUserEntity user, String roleName) {
        RoleEntity role = findByName(roleName);
        user.getRoles().remove(role);
    }

    @Override
    public void enableAppUser(Long id) {
        appUserRepository.enableAppUser(id);
    }

    @Override
    public boolean isExistsAppUserEntityByEmail(String email) {
        return appUserInfoRepository.existsAppUserInfoEntityByEmail(email);
    }

    @Override
    public boolean isExistsAppUserEntityByUsername(String username) {
        return appUserRepository.existsAppUserEntityByUsername(username);
    }

    private Boolean validateEqualOldPasswordFromDB(String oldPassword, String DBPassword) {
        return !matchPassword(oldPassword, DBPassword);
    }

    private Boolean validateEqualInputPasswords(String newPassword, String oldPassword, String DBPassword) {
        return oldPassword.equals(newPassword) || matchPassword(newPassword, DBPassword);
    }

    private boolean matchPassword(String currentPassword, String DBPassword) {
        return encoder.matches(currentPassword,DBPassword);
    }

    private AppUserEntity mapToNewAppUserEntityFromRequest(RegistrationRequest request){
        return mapperUtil.mapToNewAppUserEntityFromRequest(request);
    }

    private void saveUser(AppUserEntity user){
        appUserRepository.save(user);
    }

    private void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenService.saveConfirmationToken(token);
    }

    private AppUser mapToAppUserWithParams(AppUserEntity user){
        return mapperUtil.mapToAppUserWithParams(user);
    }

    private List<AppUserEntity> findAllUsers(){
        return appUserRepository.findAll();
    }

    private RoleEntity findByName(String role){
        return roleRepository.findByName(role);
    }
}