package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.advert.Available;
import ru.danis0n.avitoclone.entity.advert.AdvertAvailable;
import ru.danis0n.avitoclone.dto.advert.Advert;
import ru.danis0n.avitoclone.service.advert.AdvertService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/advert")
@RequiredArgsConstructor
public class AdvertController {

    private final AdvertService advertService;

    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String create(HttpServletRequest request,
                         @RequestParam("title") String title,
                         @RequestParam("location") String location,
                         @RequestParam("description") String description,
                         @RequestParam("price") BigDecimal price,
                         @RequestParam("files") MultipartFile[] files,
                         @RequestParam("titleType") String titleType,
                         @RequestParam("mainType") String mainType,
                         @RequestParam("subType") String subType,
                         @RequestParam("brandType") String brandType) {
        return advertService.create(request,title,location,description,price,files,mainType,brandType,titleType,subType);
    }

    @PostMapping("/update/{id}")
    public String update(HttpServletRequest request,
                         @PathVariable Long id,
                         @RequestParam("title") String title,
                         @RequestParam("location") String location,
                         @RequestParam("description") String description,
                         @RequestParam("price") BigDecimal price,
                         @RequestParam("files")MultipartFile[] files) {
        return advertService.update(request,id,title,location,description,price,files);
    }

    @GetMapping("/get/all")
    public List<Advert> getAll(){
        return advertService.getAll();
    }

    @GetMapping("/get/latest")
    public List<Advert> getLatest() {
        final Long quantity = 12L;
        return advertService.getLatest(quantity);
    }

    @GetMapping("/get/examples")
    public List<Advert> getExamples() {
        return advertService.getExamples();
    }

    @GetMapping("/get/user/{id}")
    public List<Advert> getAllByUserId(@PathVariable Long id){
        return advertService.getAllByUser(id);
    }

    @PostMapping("/get/params")
    public List<Advert> getAllByParams(HttpServletRequest request){
        return advertService.getByParams(request);
    }

    @PostMapping("/get/available")
    public List<Available> getAvailableQuantity(HttpServletRequest request) {
        return advertService.getAvailableQuantity(request);
    }

    @GetMapping("/get/{id}")
    public Advert getById(@PathVariable Long id){
        return advertService.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request){
        return advertService.deleteById(request,id);
    }

    @PostMapping("/hide/advert/{id}")
    public ResponseEntity<String> hideAll(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok().body(advertService.hideAllUserAdvertsByUserId(id,request));
    }

    @PostMapping("/unhide/advert/{id}")
    public ResponseEntity<String> unHideAll(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok().body(advertService.unHideAllUserAdvertsByUserId(id,request));
    }

    @PostMapping("/hide/advert/{id}/user/{userId}")
    public ResponseEntity<String> hideById(@PathVariable Long id, @PathVariable Long userId, HttpServletRequest request) {
        return ResponseEntity.ok().body(advertService.hideUserAdvertByUserId(userId,id,request));
    }

    @PostMapping("/unhide/advert/{id}/user/{userId}")
    public ResponseEntity<String> unHideById(@PathVariable Long id, @PathVariable Long userId, HttpServletRequest request) {
        return ResponseEntity.ok().body(advertService.unHideUserAdvertByUserId(userId,id,request));
    }

}