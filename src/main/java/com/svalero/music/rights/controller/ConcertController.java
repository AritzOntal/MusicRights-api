package com.svalero.music.rights.controller;

import com.svalero.music.rights.domain.Concert;

import com.svalero.music.rights.service.ConcertService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class ConcertController {

    private final ConcertService concertService;

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping("/concerts")
    public ResponseEntity<List<Concert>> getAll(
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "performed", required = false) Boolean performed
    ) {
        return concertService.findAll(city, status, performed);
    }

    @GetMapping("/v1/concerts/{id}")
    public ResponseEntity<Concert> get( @PathVariable Long id) {
        Concert concert = concertService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(concert);
    }

    @PostMapping("/v1/concerts")
    public ResponseEntity<Concert> create(@RequestBody @Valid Concert concert) {
        Concert saved = concertService.add(concert);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/v1/concerts/{id}")
    public ResponseEntity<Concert> update(@RequestBody @Valid Concert concert, @PathVariable Long id) {
        Concert updatedConcert = concertService.edit(id, concert);
        return ResponseEntity.status(HttpStatus.OK).body(updatedConcert);
    }

    @DeleteMapping("/v1/concerts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        concertService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    //FILTRADO JPQL
    @GetMapping("/v1/concerts/by-musician/{id}")
    public ResponseEntity<List<Concert>> getByMusician(@PathVariable Long id) {
        List<Concert> concertsOfMusician = concertService.findAllbyMusicianId(id);
        return ResponseEntity.status(HttpStatus.OK).body(concertsOfMusician);
    }
}