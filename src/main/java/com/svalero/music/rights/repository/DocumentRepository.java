package com.svalero.music.rights.repository;

import com.svalero.music.rights.domain.Claim;
import com.svalero.music.rights.domain.Document;
import com.svalero.music.rights.domain.Musician;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

//TODO HACER LA CONSULTA RELACION JOIN

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query ("SELECT d FROM Document d JOIN d.claim c WHERE c.id = :id")
    Document findByClaimId(@Param ("id") Long claimId);

    @Query("SELECT d FROM Document d WHERE " +
            "(:type IS NULL OR d.type = :type) AND " +
            "(:complete IS NULL OR d.complete = :complete) AND " +
            "(:createAt IS NULL OR d.createAt = :createAt)")

    List <Document> findByFilters(String type, Boolean complete, LocalDate createAt);

    // Documentos de un músico (a través de su concierto), más recientes primero
    @Query("SELECT d FROM Document d JOIN d.concert c JOIN c.musician m WHERE m.id = :id ORDER BY d.id DESC")
    List<Document> findByMusicianId(@Param("id") Long id);

    // Documentos asociados a un concierto (para borrarlos al eliminar el concierto)
    @Query("SELECT d FROM Document d WHERE d.concert.id = :concertId")
    List<Document> findByConcertId(@Param("concertId") Long concertId);
}
