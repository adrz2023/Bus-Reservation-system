package org.befikreyatra.dao;


import org.befikreyatra.model.BusSeatTemplate;
import org.befikreyatra.repository.BusSeatTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BusSeatTemplateDao {

    @Autowired
    private BusSeatTemplateRepository repo;

    public List<BusSeatTemplate> findByBusId(int busId) {
        return repo.findByBusId(busId);
    }

    public void deleteByBusId(int busId) {
        repo.deleteByBusId(busId);
    }

    public List<BusSeatTemplate> saveAll(List<BusSeatTemplate> templates) {
        return repo.saveAll(templates);
    }

    public long countBookableByBusId(int busId) {
        return repo.countBookableByBusId(busId);
    }
}