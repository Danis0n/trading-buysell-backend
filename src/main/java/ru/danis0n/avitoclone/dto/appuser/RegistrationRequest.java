package ru.danis0n.avitoclone.dto.appuser;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationRequest {
    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;
    private MultipartFile file;
}
