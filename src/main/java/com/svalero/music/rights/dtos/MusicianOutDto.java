package com.svalero.music.rights.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicianOutDto {
    private String firstName;
    private String lastName;
    private String dni;
    private Float performaceFee;
    private Boolean affiliated;
}
