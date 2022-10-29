package ru.danis0n.avitoclone.dto.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MainType {
    private Long id;
    private String name;
}
