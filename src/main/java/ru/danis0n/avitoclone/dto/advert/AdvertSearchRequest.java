package ru.danis0n.avitoclone.dto.advert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AdvertSearchRequest {
    private String title;
    private String type;
    private String maxPrice;
    private String minPrice;
    private String location;
}
