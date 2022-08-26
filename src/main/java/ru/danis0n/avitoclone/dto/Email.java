package ru.danis0n.avitoclone.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
