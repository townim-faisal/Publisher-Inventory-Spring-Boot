package rokomari.PublisherInventory.service.admin.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rokomari.PublisherInventory.model.common.Area;

import java.util.List;

public interface AreaService {
    void createArea(Area area);

    Area getArea(int id);

    Page<Area> getAllArea(Pageable pageable);

    Page<Area> getAllAreaWithSearchKey(String searchKey, Pageable pageable);

    List<Area> getAllAreaWithKey(String searchKey);

    void delete(int id);

    List<Area> getAllArea();
}
