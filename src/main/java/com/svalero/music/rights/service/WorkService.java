package com.svalero.music.rights.service;

import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.domain.User;
import com.svalero.music.rights.domain.Work;
import com.svalero.music.rights.exception.MusicianNotFoundException;
import com.svalero.music.rights.exception.WorkNotFoundException;
import com.svalero.music.rights.repository.MusicianRepository;
import com.svalero.music.rights.repository.UserRepository;
import com.svalero.music.rights.repository.WorkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkService {

    private final WorkRepository workRepository;
    private final MusicianService musicianService;
    private final MusicianRepository musicianRepository;
    private final UserRepository userRepository;

    public WorkService(WorkRepository workRepository,
                       MusicianService musicianService,
                       MusicianRepository musicianRepository,
                       UserRepository userRepository) {
        this.workRepository = workRepository;
        this.musicianService = musicianService;
        this.musicianRepository = musicianRepository;
        this.userRepository = userRepository;
    }

    public Work add(Work work) {
        List<Musician> musicianList = work.getMusicians();

        if (musicianList != null) {

            List<Musician> musiciansDb = new ArrayList<>();

            for (Musician musician : musicianList) {
                long idMusician = musician.getId();
                Musician musicianDb = musicianRepository.findById(idMusician)
                        .orElseThrow(MusicianNotFoundException::new);
                musiciansDb.add(musicianDb);

            }
            work.setMusicians(musiciansDb);
        }
        return workRepository.save(work);
    }

    public ResponseEntity<List<Work>> findAll(Float duration, LocalDate composedAt, Boolean registred) {

        // Si el usuario autenticado es MUSICIAN, devolvemos solo sus propias obras.
        // USER y ADMIN siguen viendo todas las obras (con los filtros opcionales).
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
            List<Work> ownWorks = workRepository.findByMusicianId(musicianId);
            return new ResponseEntity<>(ownWorks, HttpStatus.OK);
        }

        List<Work> works = workRepository.findByFilters(duration, composedAt, registred);
        return new ResponseEntity<>(works, HttpStatus.OK);
    }

    public Work findById(Long id) {
        Work work = workRepository.findById(id)
                .orElseThrow(WorkNotFoundException::new);
        return work;
    }

    public List<Work> findByMusician(Long id) throws MusicianNotFoundException {
        Musician musician = musicianService.findById(id);
        if (musician == null) {
            throw new MusicianNotFoundException();
        } else {
            List<Work> works = workRepository.findByMusicianId(id);
            return works;
        }
    }

    public ResponseEntity<Work> edit(long id, Work updateWork) {
        Work work = workRepository.findById(id)
                .orElseThrow(WorkNotFoundException::new);

        work.setDuration(updateWork.getDuration());
        work.setGenre(updateWork.getGenre());
        work.setTitle(updateWork.getTitle());
        work.setIsrc(updateWork.getIsrc());

        return ResponseEntity.ok().body(workRepository.save(work));
    }

    public void delete(long id) {
        Work work = workRepository.findById(id)
                .orElseThrow(WorkNotFoundException::new);
        workRepository.delete(work);
    }
}
