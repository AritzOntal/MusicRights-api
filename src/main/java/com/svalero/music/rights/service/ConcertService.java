package com.svalero.music.rights.service;

import com.svalero.music.rights.domain.Concert;
import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.domain.User;
import com.svalero.music.rights.domain.Work;
import com.svalero.music.rights.exception.ConcertNotFoundException;
import com.svalero.music.rights.exception.MusicianNotFoundException;
import com.svalero.music.rights.exception.WorkNotFoundException;
import com.svalero.music.rights.repository.ConcertRepository;
import com.svalero.music.rights.repository.MusicianRepository;
import com.svalero.music.rights.repository.UserRepository;
import com.svalero.music.rights.repository.WorkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConcertService {

    private ConcertRepository concertRepository;
    private MusicianRepository musicianRepository;
    private WorkRepository workRepository;
    private UserRepository userRepository;

    public ConcertService(ConcertRepository concertRepository, MusicianRepository musicianRepository, WorkRepository workRepository, UserRepository userRepository) {
        this.concertRepository = concertRepository;
        this.musicianRepository = musicianRepository;
        this.workRepository = workRepository;
        this.userRepository = userRepository;
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
        Musician musicianToSet = null;

        // 1) Si el payload trae un músico con id, lo resolvemos desde la BBDD
        if (concert.getMusician() != null && concert.getMusician().getId() != null) {
            musicianToSet = musicianRepository.findById(concert.getMusician().getId())
                    .orElseThrow(MusicianNotFoundException::new);
        } else {
            // 2) Si no, auto-asociamos el músico del usuario logueado (igual que las obras)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                Optional<User> userOpt = userRepository.findByUsername(auth.getName());
                if (userOpt.isPresent() && userOpt.get().getMusician() != null) {
                    musicianToSet = userOpt.get().getMusician();
                }
            }
        }

        concert.setMusician(musicianToSet);
        concert.setWorks(resolveWorks(concert.getWorks()));
        return concertRepository.save(concert);
    }

    public ResponseEntity<List<Concert>> findAll(String city, String status, Boolean performed) {
        // Si el usuario autenticado es MUSICIAN, devolvemos solo sus propios conciertos.
        // USER y ADMIN siguen viendo todos los conciertos (con los filtros opcionales).
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isMusician = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MUSICIAN"));

        if (isMusician) {
            Optional<User> userOpt = userRepository.findByUsername(auth.getName());
            // Si el músico no tiene Musician asociado todavía, devolvemos lista vacía
            if (userOpt.isEmpty() || userOpt.get().getMusician() == null) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }
            Long musicianId = userOpt.get().getMusician().getId();
            return new ResponseEntity<>(concertRepository.findByMusicianId(musicianId), HttpStatus.OK);
        }

        List<Concert> concerts = concertRepository.findByFilters(city, status, performed);
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
        // El dueño del concierto no se cambia al editar: conserva su músico actual.
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
