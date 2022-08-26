package ru.danis0n.avitoclone.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    private String to;
    private String createdBy;
    private String advertName;
    private String title;
    private String description;
    private float rating;

}
