package ru.danis0n.avitoclone.dto.appuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InfoRequest {
    private String name;
    private String email;
    private String phone;
}
