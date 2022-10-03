package ru.danis0n.avitoclone.dto.comment;

import lombok.*;

@Getter
@Setter
@Builder
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
