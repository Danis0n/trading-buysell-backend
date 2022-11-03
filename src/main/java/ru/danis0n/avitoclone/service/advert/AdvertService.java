package ru.danis0n.avitoclone.service.advert;

import org.springframework.web.multipart.MultipartFile;
import ru.danis0n.avitoclone.dto.advert.Available;
import ru.danis0n.avitoclone.dto.advert.Advert;
import ru.danis0n.avitoclone.dto.type.CustomType;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface AdvertService {
    String create(HttpServletRequest request, String title, String location, String description, BigDecimal price, MultipartFile[] files, String mainType, String brandType, String titleType, String subType);
    String update(HttpServletRequest request, Long id, String title, String location, String description, BigDecimal price, MultipartFile[] files);
    Advert getById(Long id);
    List<Advert> getAllByUser(Long id);
    List<Advert> getByParams(HttpServletRequest request);
    List<Available> getAvailableQuantityBrand(HttpServletRequest request);
    List<Available> getAvailableQuantitySub(HttpServletRequest request);
    List<Available> getAvailableQuantityMain(HttpServletRequest request);

    List<Advert> getAll();
    List<Advert> getLatest(Long quantity);
    List<Advert> getExamples();
    String deleteById(HttpServletRequest request,Long id);
    String hideUserAdvertByUserId(Long userId, Long advertId, HttpServletRequest request);
    String unHideUserAdvertByUserId(Long userId, Long advertId, HttpServletRequest request);
    String hideAllUserAdvertsByUserId(Long userId, HttpServletRequest request);
    String unHideAllUserAdvertsByUserId(Long userId, HttpServletRequest request);
    List<CustomType> getBrandTypeByTitleType(String id);
    List<CustomType> getSubTypeByTitleType(String id);
    List<CustomType> getMainTypeByTitleType(String id);

}
