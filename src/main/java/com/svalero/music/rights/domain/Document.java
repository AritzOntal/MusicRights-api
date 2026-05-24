package com.svalero.music.rights.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "documents")

public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String filename;

    @Positive
    @Column
    private Long size;

    @PastOrPresent
    @Column(name = "create_at")
    private LocalDate createAt;

    @Column
    private Boolean complete;

    @DecimalMax("100.0")
    @Column
    private float completionPercentage;

    // Clave (key) del objeto en S3 donde está almacenado el PDF generado.
    @JsonIgnore
    @Column(name = "s3_key")
    private String s3Key;

    @JsonIgnoreProperties({"musician", "works"})
    @OneToOne
    @JoinColumn(name = "claim_id", unique = true)
    private Claim claim;

    // Concierto al que pertenece este documento (para el PDF de SGAE).
    @JsonIgnoreProperties({"musician", "works"})
    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

}
