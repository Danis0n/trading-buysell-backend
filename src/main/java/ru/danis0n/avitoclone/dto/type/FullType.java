package ru.danis0n.avitoclone.dto.type;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullType {
    private MainType mainType;
    private TitleType titleType;
    private SubType subType;
    private BrandType brandType;
}
