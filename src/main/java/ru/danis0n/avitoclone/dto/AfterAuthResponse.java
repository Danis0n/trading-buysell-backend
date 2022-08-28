package ru.danis0n.avitoclone.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public class AfterAuthResponse {

    private Map<String,String> tokens;
    private String username;
    private String email;

}
