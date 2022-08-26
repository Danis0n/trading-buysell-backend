package ru.danis0n.avitoclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long id;
    private String to;
    private String createdBy;
    private String advertName;
    private String title;
    private String description;
    private float rating;
}
