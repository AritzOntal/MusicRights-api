package com.svalero.music.rights.service;

import com.svalero.music.rights.domain.Concert;
import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.domain.Work;
import com.svalero.music.rights.exception.ConcertNotFoundException;
import com.svalero.music.rights.exception.MusicianNotFoundException;
import com.svalero.music.rights.exception.WorkNotFoundException;
import com.svalero.music.rights.repository.ConcertRepository;
import com.svalero.music.rights.repository.MusicianRepository;
import com.svalero.music.rights.repository.WorkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConcertService {

    private ConcertRepository concertRepository;
    private MusicianRepository musicianRepository;
    private WorkRepository workRepository;

    public ConcertService(ConcertRepository concertRepository, MusicianRepository musicianRepository, WorkRepository workRepository) {
        this.concertRepository = concertRepository;
        this.musicianRepository = musicianRepository;
        this.workRepository = workRepository;
    }

    // Resuelve el setlist: por cada obra recibida (normalmente solo con id),
    // recupera la entidad gestionada desde la base de datos.
    private List<Work> resolveWorks(List<Work> works) {
        if (works == null || works.isEmpty()) {
            return new ArrayList<>();
        }
        List<Work> managed = new ArrayList<>();
        for (Work w : works) {
            Work workDb = workRepository.findById(w.getId())
                    .orElseThrow(WorkNotFoundException::new);
            managed.add(workDb);
        }
        return managed;
    }

    public Concert add(Concert concert) {
        Long idMusician = concert.getMusician().getId();

        if (idMusician != null) {
            Musician musicianDb = musicianRepository.findById(idMusician)
                    .orElseThrow(MusicianNotFoundException::new);
            concert.setMusician(musicianDb);
        }
        concert.setWorks(resolveWorks(concert.getWorks()));
        return concertRepository.save(concert);
    }

    public ResponseEntity<List<Concert>> findAll(String city, String status, Boolean performed) {
        List<Concert> concerts;
        concerts = concertRepository.findByFilters(city, status, performed);
        return new ResponseEntity<>(concerts, HttpStatus.OK);
    }

    public Concert findById(long id) {
        concertRepository.findById(id)
                .orElseThrow(ConcertNotFoundException::new);
        return concertRepository.findById(id).get();
    }

    public List<Concert> findAllbyMusicianId(Long id) {
        List<Concert> allConcerts = concertRepository.findByMusicianId(id);
        return allConcerts;
    }
    //LLAMADA PARA RETORNAR TODOS LOS CONCIERTOS DE UN MUSICO

    public Concert edit(long id, Concert updateConcert) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(ConcertNotFoundException::new);

        concert.setShowTitle(updateConcert.getShowTitle());
        concert.setCity(updateConcert.getCity());
        concert.setProvince(updateConcert.getProvince());
        concert.setTicketPrice(updateConcert.getTicketPrice());
        concert.setLongitude(updateConcert.getLongitude());
        concert.setPerformed(updateConcert.isPerformed());
        concert.setLatitude(updateConcert.getLatitude());
        concert.setStatus(updateConcert.getStatus());
        concert.setMusician(updateConcert.getMusician());
        concert.setDate(updateConcert.getDate());

        // Campos nuevos para el formulario de SGAE
        concert.setTime(updateConcert.getTime());
        concert.setVenueName(updateConcert.getVenueName());
        concert.setVenueAddress(updateConcert.getVenueAddress());
        concert.setCapacity(updateConcert.getCapacity());
        concert.setVenueOwner(updateConcert.getVenueOwner());
        concert.setPerformers(updateConcert.getPerformers());
        concert.setTicketClass(updateConcert.getTicketClass());
        concert.setTotalTickets(updateConcert.getTotalTickets());
        concert.setTariffType(updateConcert.getTariffType());
        concert.setWorks(resolveWorks(updateConcert.getWorks()));

        concertRepository.save(concert);
        return concert;
    }

    public void delete(long id) {
        concertRepository.findById(id)
                .orElseThrow(ConcertNotFoundException::new);
        concertRepository.deleteById(id);
    }
}
