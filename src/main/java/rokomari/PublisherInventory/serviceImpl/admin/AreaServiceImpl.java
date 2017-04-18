package rokomari.PublisherInventory.serviceImpl.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.common.Area;
import rokomari.PublisherInventory.repository.admin.AreaRepository;
import rokomari.PublisherInventory.service.admin.services.AreaService;

import java.util.List;

@Component(value = "AreaService")
@Transactional
public class AreaServiceImpl implements AreaService {

    @Autowired
    AreaRepository areaRepository;

    @Override
    public void createArea(Area area) {
        areaRepository.save(area);
    }

    @Override
    public Area getArea(int id) {
        return areaRepository.findOne(id);
    }

    @Override
    public Page<Area> getAllArea(Pageable pageable) {
        return areaRepository.findAll(pageable);
    }

    @Override
    public Page<Area> getAllAreaWithSearchKey(String searchKey, Pageable pageable) {
        return areaRepository.findByNameContainingIgnoreCase(searchKey, pageable);
    }

    @Override
    public List<Area> getAllAreaWithKey(String searchKey) {
        return areaRepository.findByNameContainingIgnoreCase(searchKey);
    }

    @Override
    public void delete(int id) {
        areaRepository.delete(id);
    }

    @Override
    public List<Area> getAllArea() {
        return (List<Area>) areaRepository.findAll();
    }
}
