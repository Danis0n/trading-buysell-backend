package ru.danis0n.avitoclone.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Image {

    private String id;
    private String name;
    private Long size;
    private String url;
    private String contentType;

}

