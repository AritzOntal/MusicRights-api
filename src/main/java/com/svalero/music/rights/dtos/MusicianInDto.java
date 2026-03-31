package com.svalero.music.rights.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicianInDto {
    private String firstName;
    private String lastName;
    private String dni;
}
