package com.svalero.music.rights.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicianUpdateConditionsDto {
    @Column(nullable = false)
    private Boolean affiliated;

    @Positive
    @Column
    private Float performanceFee;
}
