package org.jsp.dao;

import org.jsp.model.Ticket;
import org.jsp.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TicketDao {
@Autowired
	private TicketRepository ticketRepository;
		public Ticket saveTicket(Ticket ticket) {
			return ticketRepository.save(ticket);
		}
	}

