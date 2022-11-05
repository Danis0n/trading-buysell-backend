package ru.danis0n.avitoclone.dto.advert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Location {
    private String name;
    private String description;
}
