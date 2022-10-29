package ru.danis0n.avitoclone.dto.type;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class TypeRequest {
    private String titleType;
    private String[] mainType;
    private String subType;
    private String[] brandType;
}
