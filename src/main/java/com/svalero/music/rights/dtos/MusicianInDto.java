package com.svalero.music.rights.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicianInDto {
    private String firstName;
    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;
    @Pattern(regexp = "^\\d{8}[A-Za-z]$", message = "El DNI debe tener 8 dígitos seguidos de una letra")
    private String dni;
}
