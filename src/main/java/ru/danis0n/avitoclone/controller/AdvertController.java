package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.Advert;
import ru.danis0n.avitoclone.service.advert.AdvertService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/advert")
@RequiredArgsConstructor
public class AdvertController {

    private final AdvertService advertService;

    @PostMapping("/create")
    public String create(HttpServletRequest request,
                         @RequestParam("title") String title,
                         @RequestParam("location") String location,
                         @RequestParam("description") String description,
                         @RequestParam("price") String price,
                         @RequestParam("files")MultipartFile[] files,
                         @RequestParam("type") String type){
        return advertService.create(request,title,location,description,price,files,type);
    }

    @PostMapping("/update/{id}")
    public String update(HttpServletRequest request,
                         @PathVariable Long id,
                         @RequestParam("title") String title,
                         @RequestParam("location") String location,
                         @RequestParam("description") String description,
                         @RequestParam("price") String price,
                         @RequestParam("files")MultipartFile[] files,
                         @RequestParam("type") String type){
        return advertService.update(request, id, title,location,description,price,files,type);
    }

    @GetMapping("/get/all")
    public List<Advert> getAll(){
        return advertService.getAll();
    }

    @GetMapping("/get/user/{id}")
    public List<Advert> getAllByUsername(@PathVariable String id){
        return advertService.getAllByUser(id);
    }

    @GetMapping("/get/type/{id}")
    public List<Advert> getAllByType(@PathVariable String id){
        return advertService.getAllByType(id);
    }

    @GetMapping("/get/{id}")
    public Advert getById(@PathVariable Long id){
        return advertService.getById(id);
    }

}