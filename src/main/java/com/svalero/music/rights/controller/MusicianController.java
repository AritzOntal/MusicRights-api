package com.svalero.music.rights.controller;

import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.dtos.DeleteResponseDto;
import com.svalero.music.rights.dtos.MusicianInDto;
import com.svalero.music.rights.dtos.MusicianOutDto;
import com.svalero.music.rights.dtos.MusicianUpdateConditionsDto;
import com.svalero.music.rights.service.MusicianService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/")
public class MusicianController {


    private final MusicianService musicianService;

    public MusicianController(MusicianService musicianService) {
        this.musicianService = musicianService;
    }

    @GetMapping("/v1/musicians")
    public ResponseEntity<List<Musician>> getALl(
            @RequestParam(value = "performanceFee", required = false) Float performanceFee,
            @RequestParam(value = "affiliated", required = false) Boolean affiliated,
            @RequestParam(value = "birthDate", required = false) LocalDate birthDate
    ) {
        return musicianService.findAll(performanceFee, affiliated, birthDate);
    }

    @GetMapping("/v1/musicians/{id}")
    public ResponseEntity<Musician> get(@PathVariable Long id) {
        Musician musician = musicianService.findById(id);

        return ResponseEntity.ok().body(musician);
    }

    @GetMapping("/v2/musicians/{id}")
    public ResponseEntity<MusicianOutDto> getV2(@PathVariable Long id) {
        MusicianOutDto dtoOut = musicianService.findByIdV2(id);

        return ResponseEntity.ok().body(dtoOut);
    }


    @PostMapping("/v1/musicians")
    public ResponseEntity<Musician> create(@RequestBody @Valid Musician musician) {
        Musician saved = musicianService.add(musician);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/v2/musicians")
    public ResponseEntity<MusicianOutDto> createV2(@RequestBody @Valid MusicianInDto musicianDto) {
        MusicianOutDto outDto = musicianService.addV2(musicianDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(outDto);
    }


    @PutMapping("/v1/musicians/{id}")
    public ResponseEntity<Musician> edit(@PathVariable Long id, @Valid @RequestBody Musician musician) {
        musicianService.update(id, musician);

        return ResponseEntity.ok().body(musician);
    }

    @PutMapping("/v2/musicians/{id}")
    public ResponseEntity<MusicianOutDto> editV2(@PathVariable Long id, @Valid @RequestBody MusicianUpdateConditionsDto updatedMusician) {
        MusicianOutDto outDto = musicianService.updateV2(id, updatedMusician);

        return ResponseEntity.ok().body(outDto);
    }

    @DeleteMapping("/v1/musicians/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        musicianService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/v2/musicians/{id}")
    public ResponseEntity<DeleteResponseDto> deleteV2(@PathVariable long id) {
        DeleteResponseDto musicanDel = musicianService.deleteV2(id);

        return ResponseEntity.ok().body(musicanDel);
    }

}