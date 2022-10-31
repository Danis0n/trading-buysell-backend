package ru.danis0n.avitoclone.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class Notification {
    private Long id;
    private String message;
    private String dateOfCreation;
    private boolean seenByUser;
    private String userId;
}