package ru.danis0n.avitoclone.service.advert;

import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.advert.Advert;
import ru.danis0n.avitoclone.dto.advert.AdvertType;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface AdvertService {

    String create(HttpServletRequest request, String title, String location, String description, BigDecimal price, MultipartFile[] files, String type);
    String update(HttpServletRequest request, Long id, String title, String location, String description, BigDecimal price, MultipartFile[] files, String type);
    Advert getById(Long id);
    List<Advert> getAllByType(String type);
    List<Advert> getAllByUser(String username);
    List<Advert> getByParams(HttpServletRequest request);
    List<Advert> getAll();
    List<Advert> getLatest(Long quantity);
    List<Advert> getExamples();
    void createType(AdvertType auto);
    void addTypeToAdvert(String type, Long id);
    void removeTypeFromAdvert(String type, Long id);
    String deleteById(HttpServletRequest request,Long id);
}
