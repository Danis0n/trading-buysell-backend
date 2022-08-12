package ru.danis0n.avitoclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    private Long id;

    private String name;

    private String username;

    private String password;

    private String phoneNumber;

    private String email;

    private boolean enabled;

    private boolean locked;

    private LocalDateTime dateOfCreated;

    private Collection<Role> roles = new ArrayList<>();

    private List<Advert> adverts = new ArrayList<>();

    public void addRoleToAppUser(Role role){
        roles.add(role);
    }

    public void addAdvertToAppUser(Advert advert){
        adverts.add(advert);
    }

}
