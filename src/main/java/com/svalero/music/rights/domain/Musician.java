package com.svalero.music.rights.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "musicians")

public class Musician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(nullable = false)
    private Boolean affiliated;

    @Pattern(
            regexp = "^\\d{8}[A-Za-z]$",
            message = "El DNI debe tener 8 dígitos seguidos de una letra"
    )
    @Column(nullable = false) //CONTROLAREMOS CON VALIDACIONS Y EXCEPCIONES
    private String dni;

    @Positive
    @Column
    private Float performanceFee;

    @Column(name = "affiliated_number")
    private long affiliatedNumber;
    // RELACIONAR CON UNA LISTA DE WORKS POR MUSICO (List<Work)
    //NO VUELVO A CREAR LA TABLA, UTIULIZO MAPPEDBY
    @JsonIgnoreProperties("musicians")
    @ManyToMany(mappedBy = "musicians")
    private List<Work> works;

    @JsonIgnore
    @OneToMany(mappedBy = "musician")
    private List<Claim> claims;

    @OneToOne(mappedBy = "musician", optional = true)
    private User user;
}
