package com.svalero.music.rights.controller;

import com.svalero.music.rights.domain.Claim;
import com.svalero.music.rights.domain.Concert;
import com.svalero.music.rights.domain.Document;
import com.svalero.music.rights.exception.ClaimNotFoundException;
import com.svalero.music.rights.exception.ConcertNotFoundException;
import com.svalero.music.rights.exception.DocumentNotFoundException;
import com.svalero.music.rights.exception.ErrorResponse;
import com.svalero.music.rights.repository.DocumentRepository;
import com.svalero.music.rights.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/v1/documents")
    public ResponseEntity<List<Document>> getAll(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "complete", required = false) Boolean complete,
            @RequestParam(value = "createAd", required = false) LocalDate createAd
    ) {
        return documentService.findAll(type, complete, createAd);
    }

    @GetMapping("/v1/documents/{id}")
    public ResponseEntity<Document> get(@PathVariable Long id) {
        Document document = documentService.findById(id);
        return ResponseEntity.ok().body(document);
    }

    @PostMapping("/v1/documents")
    public ResponseEntity<Document> create(@RequestBody @Valid Document document) {
        documentService.add(document);
        Document saved = documentService.add(document);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); //ES NECESARIO PARA QUE ME DE EL 201 (CREATED)
                                                                        // EL PERFORME SE FIJA EN ESTA RESPUESTA!!!
    }

    @PutMapping("/v1/documents/{id}")
    public ResponseEntity<Document> update(@RequestBody @Valid Document document, @PathVariable Long id) {
        documentService.edit(id, document);
        return ResponseEntity.ok().body(document);
    }

    @DeleteMapping("/v1/documents/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //FILTRADOS
    @GetMapping("/v1/documents/by-claim/{id}")
    public ResponseEntity<Document> getByClaim(@PathVariable Long id) throws ClaimNotFoundException {
        Document documentOfClaim = documentService.findByClaim(id);
        return ResponseEntity.ok().body(documentOfClaim);
    }

}
