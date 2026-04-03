package org.befikreyatra.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.befikreyatra.util.TripSeatStatus;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"trip_id", "seatCode"})
        }
)
public class TripSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    @JsonIgnore
    private Trip trip;

    @Column(nullable = false)
    private String seatCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripSeatStatus status;

    private LocalDateTime heldUntil; // optional (for HELD UX)

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    @JsonIgnore
    private Ticket ticket;
}