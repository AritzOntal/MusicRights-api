package com.svalero.music.rights.controller;

import com.svalero.music.rights.domain.Document;
import com.svalero.music.rights.dtos.SgaeDocumentResponse;
import com.svalero.music.rights.service.SgaeDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SgaeDocumentController {

    private final SgaeDocumentService sgaeDocumentService;

    public SgaeDocumentController(SgaeDocumentService sgaeDocumentService) {
        this.sgaeDocumentService = sgaeDocumentService;
    }

    // Genera el PDF de SGAE del concierto, lo guarda en S3 y devuelve metadatos + URL de descarga
    @PostMapping("/v1/concerts/{id}/sgae-document")
    public ResponseEntity<SgaeDocumentResponse> generate(@PathVariable Long id) throws IOException {
        Document document = sgaeDocumentService.generateForConcert(id);
        String url = sgaeDocumentService.presignedUrl(document);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(document, url));
    }

    // Lista los documentos del músico logueado (para la página "Documentos")
    @GetMapping("/v1/documents/mine")
    public ResponseEntity<List<Document>> mine() {
        return ResponseEntity.ok().body(sgaeDocumentService.findForCurrentMusician());
    }

    // Devuelve una URL de descarga prefirmada (fresca) para un documento ya generado
    @GetMapping("/v1/documents/{id}/download")
    public ResponseEntity<SgaeDocumentResponse> download(@PathVariable Long id) {
        Document document = sgaeDocumentService.find(id);
        String url = sgaeDocumentService.presignedUrl(document);
        return ResponseEntity.ok().body(toResponse(document, url));
    }

    // Borra un documento del músico logueado (objeto en S3 + registro)
    @DeleteMapping("/v1/documents/mine/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sgaeDocumentService.deleteForCurrentMusician(id);
        return ResponseEntity.noContent().build();
    }

    private SgaeDocumentResponse toResponse(Document d, String url) {
        return new SgaeDocumentResponse(
                d.getId(),
                d.getFilename(),
                d.getSize(),
                d.getCompletionPercentage(),
                Boolean.TRUE.equals(d.getComplete()),
                url
        );
    }
}
