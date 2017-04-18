package rokomari.PublisherInventory.serviceImpl.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.common.City;
import rokomari.PublisherInventory.repository.admin.CityRepository;
import rokomari.PublisherInventory.service.admin.services.CityService;

import java.util.List;

@Component(value = "CityService")
@Transactional
public class CityServiceImpl implements CityService {

    @Autowired
    CityRepository cityRepository;

    @Override
    public void createCity(City city) {
        cityRepository.save(city);
    }

    @Override
    public City getCity(int id) {
        return cityRepository.findOne(id);
    }

    @Override
    public Page<City> getAllCity(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Override
    public Page<City> getAllCityWithSearchKey(String searchKey, Pageable pageable) {
        return cityRepository.findByNameContainingIgnoreCase(searchKey, pageable);
    }

    @Override
    public List<City> getAllCityWithKey(String searchKey) {
        return cityRepository.findByNameContainingIgnoreCase(searchKey);
    }

    @Override
    public void delete(int id) {
        cityRepository.delete(id);
    }

    @Override
    public List<City> getAllCity() {
        return (List<City>) cityRepository.findAll();
    }
}
