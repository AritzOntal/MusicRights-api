package com.svalero.music.rights.controller;

import com.svalero.music.rights.domain.Claim;
import com.svalero.music.rights.domain.Work;
import com.svalero.music.rights.exception.ErrorResponse;
import com.svalero.music.rights.exception.MusicianNotFoundException;
import com.svalero.music.rights.exception.WorkNotFoundException;
import com.svalero.music.rights.service.DocumentService;
import com.svalero.music.rights.service.WorkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class WorkController {

    private final WorkService workService;

    public WorkController(WorkService workService) {
        this.workService = workService;

    }

    @RequestMapping("api/v1")
    @GetMapping("/works")
    public ResponseEntity<List<Work>> getAll(
            @RequestParam(value = "duration", required = false) Float duration,
            @RequestParam(value = "composedAt", required = false) LocalDate composedAt,
            @RequestParam(value = "registred", required = false) Boolean registred
    ) {
        return workService.findAll(duration, composedAt, registred);
    }

    @GetMapping("/works/{id}")
    public ResponseEntity<Work> get(@PathVariable Long id) {
        Work work = workService.findById(id);
        return ResponseEntity.ok().body(work);
    }

    @PostMapping("/works")
    public ResponseEntity<Work> create(@RequestBody @Valid Work work) {
        workService.add(work);
        return ResponseEntity.status(HttpStatus.CREATED).body(work);
    }

    @PutMapping("/works/{id}")
    public ResponseEntity<Work> update(@PathVariable Long id, @RequestBody @Valid Work work) {
        workService.edit(id, work);
        return ResponseEntity.ok().body(work);
    }

    @DeleteMapping("/works/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
