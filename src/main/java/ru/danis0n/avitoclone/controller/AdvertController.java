package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.danis0n.avitoclone.service.advert.AdvertService;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdvertController {

    private final AdvertService advertService;



}
