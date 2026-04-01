package org.befikreyatra.repository;

import org.befikreyatra.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	
@Query("select t from Ticket t where t.user.id=?1")
    List<Ticket> findByUserId(int UserId);


   @Query("select coalesce(sum(t.cost),0) from Ticket t")
    double totalRevenue();

    @Query("select coalesce(sum(t.cost),0) from Ticket t where t.dateOfBooking >= ?1")
    double revenueFrom(LocalDate fromDate);

    @Query("select count(t) from Ticket t")
    long totalTickets();

    @Query("select count(t) from Ticket t where t.dateOfBooking >= ?1")
    long ticketsFrom(LocalDate fromDate);

    @Query("select count(t) from Ticket t where t.status = 'CANCELLED'")
    long cancelledTickets();

    @Query("select count(t) from Ticket t where t.status = 'CANCELLED' and t.dateOfBooking >= ?1")
    long cancelledTicketsFrom(LocalDate fromDate);

    @Query("select count(t)>0 from Ticket t where t.user.id=?1 and t.bus.id=?2 and t.status='EXPIRED'")
    boolean hasExpiredTicketForBus(int userId, int busId);
}
