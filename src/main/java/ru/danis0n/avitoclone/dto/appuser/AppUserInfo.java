package ru.danis0n.avitoclone.dto.appuser;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AppUserInfo {

    private String name;
    private String email;
    private String phone;
    private Float rating;
    private LocalDateTime dateOfCreation;
}
