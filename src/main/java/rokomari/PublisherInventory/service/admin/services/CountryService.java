package rokomari.PublisherInventory.service.admin.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.common.Country;

import java.util.List;

public interface CountryService {
    void createCountry(Country city);

    Country getCountry(int id);

    Page<Country> getAllCountry(Pageable pageable);

    Page<Country> getAllCountryWithSearchKey(String searchKey, Pageable pageable);

    List<Country> getAllCountryWithKey(String searchKey);

    void delete(int id);

    List<Country> getAllCountry();
}
