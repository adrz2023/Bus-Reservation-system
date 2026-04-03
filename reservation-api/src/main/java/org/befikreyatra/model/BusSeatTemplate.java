package org.befikreyatra.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.befikreyatra.util.SeatType;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"bus_id", "seatCode"}),
                @UniqueConstraint(columnNames = {"bus_id", "rowIndex", "colIndex", "deck"})
        }
)
public class BusSeatTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    @JsonIgnore
    private Bus bus;

    @Column(nullable = false)
    private String seatCode; // e.g. A1

    @Column(nullable = false)
    private int rowIndex;

    @Column(nullable = false)
    private int colIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType; // SEAT / EMPTY / DRIVER

    private Integer deck; // optional, nullable

    @Column(nullable = false)
    private boolean isBookable;
}