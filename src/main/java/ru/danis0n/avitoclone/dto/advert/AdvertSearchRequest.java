package ru.danis0n.avitoclone.dto.advert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.danis0n.avitoclone.dto.type.TypeRequest;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AdvertSearchRequest {
    private String title;
    private TypeRequest type;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private String[] locations;
}
