package org.befikreyatra.repository;

import org.befikreyatra.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	
@Query("select t from Ticket t where t.user.id=?1")
    List<Ticket> findByUserId(int UserId);
}
