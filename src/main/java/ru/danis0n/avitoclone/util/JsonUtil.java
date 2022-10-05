package ru.danis0n.avitoclone.util;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@AllArgsConstructor
public class JsonUtil {

    private final Gson gson;

    public String getJson(HttpServletRequest request){
        try {
            return request.getReader().lines().collect(
                    Collectors.joining()).replace(" ", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
