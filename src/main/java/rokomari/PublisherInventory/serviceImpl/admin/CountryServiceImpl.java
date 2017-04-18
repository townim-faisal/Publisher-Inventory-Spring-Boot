package rokomari.PublisherInventory.serviceImpl.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.common.Country;
import rokomari.PublisherInventory.repository.admin.CountryRepository;
import rokomari.PublisherInventory.service.admin.services.CountryService;

import java.util.List;

@Component(value = "CountryService")
@Transactional
public class CountryServiceImpl implements CountryService {

    @Autowired
    CountryRepository countryRepository;

    @Override
    public void createCountry(Country country) {
        countryRepository.save(country);
    }

    @Override
    public Country getCountry(int id) {
        return countryRepository.findOne(id);
    }

    @Override
    public Page<Country> getAllCountry(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

    @Override
    public Page<Country> getAllCountryWithSearchKey(String searchKey, Pageable pageable) {
        return countryRepository.findByNameContainingIgnoreCase(searchKey, pageable);
    }

    @Override
    public List<Country> getAllCountryWithKey(String searchKey) {
        return countryRepository.findByNameContainingIgnoreCase(searchKey);
    }

    @Override
    public void delete(int id) {
        countryRepository.delete(id);
    }

    @Override
    public List<Country> getAllCountry() {
        return (List<Country>) countryRepository.findAll();
    }
}
