package org.befikreyatra.model;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.befikreyatra.util.TripStatus;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    @JsonIgnore
    private Bus bus;

    @Column(nullable = false)
    private String from_location;

    @Column(nullable = false)
    private String to_location;

    @Column(nullable = false)
    private LocalDate departureDate;

    @Column(nullable = false)
    private int availableSeats;

    @Column(nullable = false)
    private double costPerSeat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status;

    @OneToMany(mappedBy = "trip")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripSeat> tripSeats;
}