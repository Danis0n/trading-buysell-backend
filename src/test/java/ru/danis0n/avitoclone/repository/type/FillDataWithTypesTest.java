package ru.danis0n.avitoclone.repository.type;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.danis0n.avitoclone.entity.advert.AdvertEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
public class FillDataWithTypesTest {

    @PersistenceContext
    private EntityManager entityManager;

    public List<AdvertEntity> findAllBySqlNative(String query) {
        return null;
    }

    public void TestSqlNative() {

    }

}
