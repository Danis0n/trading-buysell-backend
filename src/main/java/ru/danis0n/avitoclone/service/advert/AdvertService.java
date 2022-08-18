package ru.danis0n.avitoclone.service.advert;

import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.Advert;
import ru.danis0n.avitoclone.dto.AdvertType;
import ru.danis0n.avitoclone.entity.AdvertEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AdvertService {

    String create(HttpServletRequest request, String title, String location, String description, String price, MultipartFile[] files, String type);
    String update(HttpServletRequest request, Long id, String title, String location, String description, String price, MultipartFile[] files, String type);
    Advert getById(Long id);
    List<Advert> getAllByType(String type);
    List<Advert> getAllByUser(String username);
    List<Advert> getAll();
    void createType(AdvertType auto);
    void addTypeToAdvert(String type, Long id);
    void removeTypeFromAdvert(String type, Long id);
    String deleteById(HttpServletRequest request,Long id);
}
