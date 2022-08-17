package ru.danis0n.avitoclone.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
