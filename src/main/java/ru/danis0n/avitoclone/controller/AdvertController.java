package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.service.advert.AdvertService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdvertController {

    private final AdvertService advertService;

    @PostMapping("/crate")
    public String create(HttpServletRequest request,
                         @RequestParam("title") String title,
                         @RequestParam("location") String location,
                         @RequestParam("title") String description,
                         @RequestParam("price") String price,
                         @RequestParam("files")MultipartFile[] files){
        return advertService.create(request,title,location,description,price,files);
    }


}