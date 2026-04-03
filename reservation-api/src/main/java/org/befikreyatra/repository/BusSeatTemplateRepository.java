package org.befikreyatra.repository;


import org.befikreyatra.model.BusSeatTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusSeatTemplateRepository extends JpaRepository<BusSeatTemplate, Integer> {

    @Query("select t from BusSeatTemplate t where t.bus.id=?1 order by coalesce(t.deck,0), t.rowIndex, t.colIndex")
    List<BusSeatTemplate> findByBusId(int busId);

    void deleteByBusId(int busId);

    @Query("select count(t) from BusSeatTemplate t where t.bus.id=?1 and t.isBookable=true")
    long countBookableByBusId(int busId);
}