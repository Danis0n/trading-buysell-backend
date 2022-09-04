package ru.danis0n.avitoclone.dto.appuser;

import lombok.*;
import ru.danis0n.avitoclone.dto.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    private Long id;
    private String username;
    private AppUserInfo info;
    private boolean enabled;
    private boolean locked;

    private Collection<Role> roles = new ArrayList<>();

    private List<String> advertIds = new ArrayList<>();

    public void addRoleToAppUser(Role role){
        roles.add(role);
    }

    public void addAdvertToAppUser(String advert){
        advertIds.add(advert);
    }

}
