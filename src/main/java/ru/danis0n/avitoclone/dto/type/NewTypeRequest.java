package ru.danis0n.avitoclone.dto.type;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewTypeRequest {
    String type;
    String name;
    String description;
}
