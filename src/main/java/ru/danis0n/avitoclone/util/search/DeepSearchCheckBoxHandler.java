package ru.danis0n.avitoclone.util.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.hql.internal.ast.SqlASTFactory;
import org.springframework.stereotype.Component;
import ru.danis0n.avitoclone.dto.type.CustomType;
import ru.danis0n.avitoclone.entity.type.CustomTypeEntity;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

import javax.persistence.EntityManager;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class DeepSearchCheckBoxHandler {

    private EntityManager em;
    private final ObjectMapperUtil mapperUtil;

    public List<CustomType> getSubTypeByTitleType(String titleType) {
        String query = "SELECT * FROM sub_" + titleType + "_type";
        return mapperUtil.mapToListCustomType(getAllByNativeQuery(query,getSql()));
    }

    public List<CustomType> getMainTypeByTitleType(String titleType) {
        String query = "SELECT * FROM main_" + titleType + "_type";
        return mapperUtil.mapToListCustomType(getAllByNativeQuery(query,getSql()));
    }

    public List<CustomType> getBrandTypeByTitleType(String titleType) {
        String query = "SELECT * FROM brand_" + titleType + "_type";
        return mapperUtil.mapToListCustomType(getAllByNativeQuery(query,getSql()));
    }

    public List<CustomType> getLocations() {
        String query = "SELECT * FROM location";
        return mapperUtil.mapToListCustomType(getAllByNativeQuery(query,getSql()));
    }

    private String getSql() {
        return "custom_type_result";
    }

    private List<CustomTypeEntity> getAllByNativeQuery(String query, String sql) {
        return em.createNativeQuery(query,sql).getResultList();
    }
}
