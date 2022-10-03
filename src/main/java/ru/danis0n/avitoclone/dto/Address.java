package ru.danis0n.avitoclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Address {
    private String city;
    private String street;
    private String building;
    private String flat;
}
