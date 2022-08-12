package ru.danis0n.avitoclone.service.appuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danis0n.avitoclone.dto.AppUser;
import ru.danis0n.avitoclone.dto.Role;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.ConfirmationToken;
import ru.danis0n.avitoclone.entity.RoleEntity;
import ru.danis0n.avitoclone.repository.AppUserRepository;
import ru.danis0n.avitoclone.repository.RoleRepository;
import ru.danis0n.avitoclone.service.advert.AdvertService;
import ru.danis0n.avitoclone.service.confirm.ConfirmationTokenService;

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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdvertService advertService;
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
        AppUserEntity entity = mapToAppUserEntity(user);
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
    public AppUser getAppUser(String username) {
        AppUserEntity entity = appUserRepository.findByUsername(username);
        return mapToAppUser(entity);
    }

    @Override
    public List<AppUser> getAppUsers() {
        List<AppUserEntity> entities = appUserRepository.findAll();
        List<AppUser> users = new ArrayList<>();
        entities.forEach(e -> {
            users.add(mapToAppUser(e));
        });

        return users;
    }

    @Override
    public void enabledAppUser(String email) {
        appUserRepository.enableAppUser(email);
    }

    private AppUser mapToAppUser(AppUserEntity entity){

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

        entity.getRoles().forEach(e ->{
            user.addRoleToAppUser(mapToRole(e));
        });

        entity.getAdverts().forEach(e -> {
            user.addAdvertToAppUser(advertService.mapToAdvert(e));
        });

        return user;
    }

    private AppUserEntity mapToAppUserEntity(AppUser user){
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

    private Role mapToRole(RoleEntity entity){
        Role role = new Role();
        role.setId(entity.getId());
        role.setName(entity.getName());
        return role;
    }

}
