package ru.danis0n.avitoclone.dto.appuser;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserInfo {

    private String name;
    private String email;
    private String phone;
    private String rating;
    private String dateOfCreation;
}
