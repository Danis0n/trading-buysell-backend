package ru.danis0n.avitoclone.dto.advert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AdvertSearchRequest {
    private String title;
    private String type;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private String location;
}
