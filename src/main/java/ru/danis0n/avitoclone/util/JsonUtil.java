package ru.danis0n.avitoclone.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JsonUtil {

    public String getJson(HttpServletRequest request){
        try {
            return request.getReader().lines().collect(
                    Collectors.joining()).replace(" ", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
