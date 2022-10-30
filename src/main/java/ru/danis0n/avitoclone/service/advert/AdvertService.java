package ru.danis0n.avitoclone.service.advert;

import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.advert.Advert;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface AdvertService {
    String create(HttpServletRequest request, String title, String location, String description, BigDecimal price, MultipartFile[] files, String mainType, String brandType, String titleType, String subType);
    String update(HttpServletRequest request, Long id, String title, String location, String description, BigDecimal price, MultipartFile[] files);
    Advert getById(Long id);
    List<Advert> getAllByUser(Long id);
    List<Advert> getByParams(HttpServletRequest request);
    List<String> getAllAvailable(HttpServletRequest request);
    List<Advert> getAll();
    List<Advert> getLatest(Long quantity);
    List<Advert> getExamples();
    String deleteById(HttpServletRequest request,Long id);
}
