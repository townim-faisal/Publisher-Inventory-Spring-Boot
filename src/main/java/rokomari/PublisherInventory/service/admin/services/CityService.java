package rokomari.PublisherInventory.service.admin.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.common.City;

import java.util.List;

public interface CityService {
    void createCity(City city);

    City getCity(int id);

    Page<City> getAllCity(Pageable pageable);

    Page<City> getAllCityWithSearchKey(String searchKey, Pageable pageable);

    List<City> getAllCityWithKey(String searchKey);

    void delete(int id);

    List<City> getAllCity();
}
