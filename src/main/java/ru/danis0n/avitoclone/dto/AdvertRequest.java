package ru.danis0n.avitoclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
public class AdvertRequest {
    private String title;
    private String location;
    private String description;
    private String type;
    private String price;
    private MultipartFile[] files;
}
