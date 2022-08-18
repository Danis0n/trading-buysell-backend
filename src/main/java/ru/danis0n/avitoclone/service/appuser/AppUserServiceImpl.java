package ru.danis0n.avitoclone.service.appuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.entity.*;
import ru.danis0n.avitoclone.repository.AppUserInfoRepository;
import ru.danis0n.avitoclone.repository.AppUserRepository;
import ru.danis0n.avitoclone.repository.RoleRepository;
import ru.danis0n.avitoclone.service.confirm.ConfirmationTokenService;
import ru.danis0n.avitoclone.util.JwtUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final AppUserInfoRepository appUserInfoRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapperUtil mapperUtil;
    private final JwtUtil jwtUtil;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserEntity user = appUserRepository.findByUsername(username);
        if(user == null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User {} found in the database",user.getUsername());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(),authorities
        );
    }

    @Override
    public String saveAppUser(AppUser user) {
        AppUserEntity entity = mapperUtil.mapToAppUserEntity(user);
        appUserRepository.save(entity);
        addRoleToAppUser(entity.getUsername(),"ROLE_NOT_CONFIRMED");

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                entity
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    @Override
    public Role saveRole(Role role) {
        RoleEntity entity = new RoleEntity();
        entity.setName(role.getName());
        roleRepository.save(entity);
        role.setId(roleRepository.findByName(role.getName()).getId());
        return role;
    }

    @Override
    public String banAppUserById(Long id, HttpServletRequest request) {
        AppUserEntity user = appUserRepository.findById(id).orElse(null);
        if(user == null){
            return "null";
        }

        String username = jwtUtil.getUsernameFromRequest(request);

        if(user.equals(appUserRepository.findByUsername(username))){
            return "You can't ban yourself";
        }

        manageBanned(user,"BAN");
        return "user '" + user.getUsername() + "' banned!";
    }

    @Override
    public String unBanAppUserById(Long id) {
        AppUserEntity user = appUserRepository.findById(id).orElse(null);
        if(user == null){
            return "null";
        }
        manageBanned(user,"UNBAN");
        return "user '" + user.getUsername() + "' un-banned!";
    }

    @Override
    public AppUser getAppUser(String username) {
        AppUserEntity entity = appUserRepository.findByUsername(username);
        return mapperUtil.mapToAppUser(entity);
    }

    @Override
    public AppUserEntity getAppUserEntity(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> getAppUsers() {
        List<AppUserEntity> entities = appUserRepository.findAll();
        List<AppUser> users = new ArrayList<>();
        entities.forEach(e -> {
            users.add(mapperUtil.mapToAppUserWithParams(e));
        });

        return users;
    }

    @Override
    public AppUser getAppUserById(Long id) {
        return mapperUtil.mapToAppUserWithParams(appUserRepository.findById(id).get());
    }

    private void manageBanned(AppUserEntity user, String bannedOrNot){
        user.getRoles().clear();
        switch (bannedOrNot){
            case "BAN":{
                lockAppUser(user.getUsername());
                addRoleToAppUser(user.getUsername(),"ROLE_BANNED");
                break;
            }
            case "UNBAN":{
                unLockAppUser(user.getUsername());
                addRoleToAppUser(user.getUsername(),"ROLE_USER");
                break;
            }
            default:{
            }
        }
    }

    @Override
    public void addRoleToAppUser(String username, String roleName) {
        AppUserEntity user = appUserRepository.findByUsername(username);
        RoleEntity role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public void removeRoleFromAppUser(String username, String roleName) {
        AppUserEntity user = appUserRepository.findByUsername(username);
        RoleEntity role = roleRepository.findByName(roleName);
        user.getRoles().remove(role);
    }

    @Override
    public void enableAppUser(String username) {
        appUserRepository.enableAppUser(username);
    }

    @Override
    public void lockAppUser(String username){
        appUserRepository.lockAppUser(username);
    }

    @Override
    public void unLockAppUser(String username){
        appUserRepository.unLockAppUser(username);
    }

    @Override
    public boolean isExistsAppUserEntityByEmail(String email) {
        return appUserInfoRepository.existsAppUserInfoEntityByEmail(email);
    }

    @Override
    public boolean isExistsAppUserEntityByUsername(String username) {
        return appUserRepository.existsAppUserEntityByUsername(username);
    }
}