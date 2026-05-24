package com.svalero.music.rights.service;

import com.svalero.music.rights.domain.Concert;
import com.svalero.music.rights.domain.Document;
import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.domain.User;
import com.svalero.music.rights.exception.ConcertNotFoundException;
import com.svalero.music.rights.exception.DocumentNotFoundException;
import com.svalero.music.rights.repository.ConcertRepository;
import com.svalero.music.rights.repository.DocumentRepository;
import com.svalero.music.rights.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Orquesta la generación del documento SGAE de un concierto:
 * rellena el PDF, lo sube a S3 y persiste un Document con sus metadatos + la key de S3.
 */
@Service
public class SgaeDocumentService {

    private static final Duration DOWNLOAD_TTL = Duration.ofMinutes(10);

    private final ConcertRepository concertRepository;
    private final DocumentRepository documentRepository;
    private final SgaePdfService sgaePdfService;
    private final S3StorageService s3StorageService;
    private final UserRepository userRepository;

    public SgaeDocumentService(ConcertRepository concertRepository,
                               DocumentRepository documentRepository,
                               SgaePdfService sgaePdfService,
                               S3StorageService s3StorageService,
                               UserRepository userRepository) {
        this.concertRepository = concertRepository;
        this.documentRepository = documentRepository;
        this.sgaePdfService = sgaePdfService;
        this.s3StorageService = s3StorageService;
        this.userRepository = userRepository;
    }

    /** Documentos del músico actualmente logueado (vacío si no es músico o no tiene ficha). */
    public List<Document> findForCurrentMusician() {
        Long musicianId = currentMusicianId();
        if (musicianId == null) {
            return List.of();
        }
        return documentRepository.findByMusicianId(musicianId);
    }

    /** Borra un documento (objeto en S3 + registro) solo si pertenece al músico logueado. */
    public void deleteForCurrentMusician(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(DocumentNotFoundException::new);

        Long ownerId = (document.getConcert() != null && document.getConcert().getMusician() != null)
                ? document.getConcert().getMusician().getId()
                : null;
        Long currentId = currentMusicianId();
        // Solo el dueño puede borrar; si no, lo tratamos como inexistente.
        if (currentId == null || !currentId.equals(ownerId)) {
            throw new DocumentNotFoundException();
        }

        if (document.getS3Key() != null) {
            s3StorageService.delete(document.getS3Key());
        }
        documentRepository.delete(document);
    }

    private Long currentMusicianId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        return userRepository.findByUsername(auth.getName())
                .map(User::getMusician)
                .map(Musician::getId)
                .orElse(null);
    }

    public Document generateForConcert(Long concertId) throws IOException {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(ConcertNotFoundException::new);

        byte[] pdf = sgaePdfService.fill(concert);

        String key = "sgae/" + concertId + "/" + UUID.randomUUID() + ".pdf";
        s3StorageService.upload(key, pdf, "application/pdf");

        float percentage = sgaePdfService.completionPercentage(concert);

        Document document = new Document();
        document.setType("SGAE_CONCERT");
        document.setFilename("sgae-concierto-" + concertId + ".pdf");
        document.setSize((long) pdf.length);
        document.setCreateAt(LocalDate.now());
        document.setCompletionPercentage(percentage);
        document.setComplete(percentage >= 100f);
        document.setS3Key(key);
        document.setConcert(concert);

        return documentRepository.save(document);
    }

    public Document find(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(DocumentNotFoundException::new);
    }

    public String presignedUrl(Document document) {
        return s3StorageService.presignedGetUrl(document.getS3Key(), document.getFilename(), DOWNLOAD_TTL);
    }
}
