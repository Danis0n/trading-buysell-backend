package ru.danis0n.avitoclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.danis0n.avitoclone.dto.appuser.AppUser;

@Setter
@Getter
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private AppUser user;

}
