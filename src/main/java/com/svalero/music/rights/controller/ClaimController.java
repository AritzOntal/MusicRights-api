package com.svalero.music.rights.controller;

import com.svalero.music.rights.domain.Claim;
import com.svalero.music.rights.exception.ErrorResponse;
import com.svalero.music.rights.exception.ClaimNotFoundException;
import com.svalero.music.rights.exception.MusicianNotFoundException;
import com.svalero.music.rights.repository.ClaimRepository;
import com.svalero.music.rights.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @RequestMapping("api/v1")
    @GetMapping("/claims")
    public ResponseEntity<List<Claim>> getAll(
            @RequestParam(value = "pending", required = false) Boolean pending,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type
    ) {
        return claimService.findAll(status, type, pending);
    }


    @GetMapping("/claims/{id}")
    public ResponseEntity<Claim> get(@PathVariable long id) throws ClaimNotFoundException {
        Claim claim = claimService.findById(id);
        return ResponseEntity.ok().body(claim);
    }

    @PostMapping("/claims")

    public ResponseEntity<Claim> create(@RequestBody @Valid Claim claim) {
        claimService.add(claim);
        return ResponseEntity.status(HttpStatus.CREATED).body(claim);
    }


    @PutMapping("/claims/{id}")
    public ResponseEntity<Claim> update(@RequestBody @Valid Claim claim, @PathVariable long id) throws ClaimNotFoundException {
        Claim updatedClaim = claimService.modify(id, claim);
        return ResponseEntity.ok().body(updatedClaim);
    }

    @DeleteMapping("/claims/{id}")
    public ResponseEntity<Void> remove(@PathVariable long id) throws ClaimNotFoundException {
        claimService.delete(id);
        return ResponseEntity.noContent().build();
    }

//FILTRADOS

    @GetMapping("/claims/by-musician/{id}")
    public ResponseEntity<List<Claim>> getByMusician(@PathVariable long id) throws MusicianNotFoundException {
        List<Claim> claimsOfMusician = claimService.findByMusicianId(id);
        return ResponseEntity.ok().body(claimsOfMusician);
    }


}
