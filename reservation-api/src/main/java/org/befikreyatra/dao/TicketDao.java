package org.befikreyatra.dao;

import org.befikreyatra.model.Ticket;
import org.befikreyatra.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketDao {
@Autowired
	private TicketRepository ticketRepository;
		public Ticket saveTicket(Ticket ticket) {
			return ticketRepository.save(ticket);
		}

		public List<Ticket> findByUserId (int userId){
			return ticketRepository.findByUserId(userId);
		}
	}


