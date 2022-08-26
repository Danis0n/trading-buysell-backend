package ru.danis0n.avitoclone.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AppUserInfo {

    private String name;
    private String email;
    private String phoneNumber;
    private Float rating;
    private LocalDateTime dateOfCreation;
}
