package ru.danis0n.avitoclone.dto.comment;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    private Long to;
    private Long createdBy;
    private String advertName;
    private String title;
    private String description;
    private float rating;

}
