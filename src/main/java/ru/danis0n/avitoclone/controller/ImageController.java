package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.advert.Image;
import ru.danis0n.avitoclone.entity.advert.ImageEntity;
import ru.danis0n.avitoclone.service.image.ImageService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            imageService.saveFile(file,null);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("File uploaded successfully: %s", file.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(String.format("Could not upload the file: %s!", file.getOriginalFilename()));
        }
    }

    @PostMapping("/uploads")
    public ResponseEntity<String> uploads(@RequestParam("files") MultipartFile[] files){
        try{
            for(MultipartFile file : files){
                upload(file);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body("All files has been uploaded!");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload the files");
        }
    }

    @GetMapping
    public List<Image> list() {
        return imageService.getAllFiles();
    }

    @GetMapping("{id}")
    public ResponseEntity<Image> getFile(@PathVariable String id) {
        Image image = null;
        image = imageService.getFileById(id);

        return ResponseEntity.ok(image);
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<byte[]> getBytesFile(@PathVariable String id){
        ImageEntity imageEntity = imageService.getSrcFileById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageEntity.getName() + "\"")
                .contentType(MediaType.valueOf(imageEntity.getContentType()))
                .body(imageEntity.getData());
    }

}
