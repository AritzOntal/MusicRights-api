package com.svalero.music.rights.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "concerts")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private String showTitle;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private String status;

    @NotNull(message = "El estado del concierto no puede ser nulo")
    @Column
    private boolean performed;

    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @Column
    private float ticketPrice;

    @Column
    private Double longitude;

    @Column
    private Double latitude;

    // --- Datos del concierto/local para el formulario de SGAE ---
    @Column
    private LocalTime time;          // Hora prevista

    @Column(name = "venue_name")
    private String venueName;        // Nombre del local

    @Column(name = "venue_address")
    private String venueAddress;     // Domicilio del local

    @Column
    private Integer capacity;        // Aforo total

    @Column(name = "venue_owner")
    private String venueOwner;       // Titular del local

    @Column
    private String performers;       // Actuantes

    @Column(name = "ticket_class")
    private String ticketClass;      // Clase de localidad (ej: General)

    @Column(name = "total_tickets")
    private Integer totalTickets;    // Número de localidades / total

    @Column(name = "tariff_type")
    private String tariffType;       // Tarifa: PERCENTAGE (8,5%) o FLAT (tanto alzado)

    // Setlist: obras ejecutadas en este concierto (campo "Títulos de las obras" del PDF)
    @JsonIgnoreProperties({"musicians"})
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "concert_work",
            joinColumns = @JoinColumn(name = "concert_id"),
            inverseJoinColumns = @JoinColumn(name = "work_id")
    )
    private List<Work> works;

    @JsonIgnoreProperties("works")
    @ManyToOne
    @JoinColumn(name = "musician_id")

    private Musician musician;
}