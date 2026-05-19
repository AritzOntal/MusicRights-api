package com.svalero.music.rights.service;


import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.domain.User;
import com.svalero.music.rights.domain.Work;
import com.svalero.music.rights.dtos.DeleteResponseDto;
import com.svalero.music.rights.dtos.MusicianInDto;
import com.svalero.music.rights.dtos.MusicianOutDto;
import com.svalero.music.rights.dtos.MusicianUpdateConditionsDto;
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

@Service
public class MusicianService {

    private MusicianRepository musicianRepository;
    private WorkRepository workRepository; //REFERENCIA AL REPOSITORIO POR AUTOWIRED
    private UserRepository userRepository;

    public MusicianService(MusicianRepository musicianRepository,
                           WorkRepository workRepository,
                           UserRepository userRepository) { //MEJOR CON CONSTRUCTOR QUE CON AUTOWIRED
        this.musicianRepository = musicianRepository;
        this.workRepository = workRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crea la ficha de músico del usuario que está logueado en ese momento
     * y promociona al usuario a ROLE_MUSICIAN. Si el usuario ya tenía un
     * Musician asociado, lanza un error.
     */
    public Musician becomeMusician(Musician input) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getMusician() != null) {
            throw new RuntimeException("El usuario ya tiene una ficha de músico asociada");
        }

        // Defaults seguros por si el cliente no los envía
        if (input.getAffiliated() == null) {
            input.setAffiliated(false);
        }
        input.setId(null);
        input.setWorks(null);
        input.setClaims(null);

        Musician saved = musicianRepository.save(input);

        // Asociamos el musician al user y subimos su rol
        user.setMusician(saved);
        user.setRole("ROLE_MUSICIAN");
        userRepository.save(user);

        return saved;
    }

    //TODO ENTEDER COMO LO METE TODO EN LA TABLA N a N
    public Musician add(Musician musician) {

        List<Work> managedWorks = new ArrayList<>();

        if (musician.getWorks() != null) {      //musician.getWorks ---> ids que el cliente dice que quiere relacionar (los del Array)
            for (Work w : musician.getWorks()) {
                Work workDb = workRepository.findById(w.getId()) //Saca el ID de cada Work pedido por el cliente y lo checkea en BDD
                        .orElseThrow(WorkNotFoundException::new); //Si no encuentra nigun lanza exception

                workDb.getMusicians().add(musician);  // Como Work es el propietario de la talba intermedia le añadimos el musico a su lista de musicos de Work
                managedWorks.add(workDb); //A la lista creada arriba le añadimos el compacto (el musico tambien)
            }
        }
        musician.setWorks(managedWorks); //Al músico le añadimos todas las works que hemos pillado
        return musicianRepository.save(musician);
    }

    public MusicianOutDto addV2(MusicianInDto inDto) {
        Musician musician = new Musician();

        musician.setFirstName(inDto.getFirstName());
        musician.setLastName(inDto.getLastName());
        musician.setDni(inDto.getDni());
        musician.setPerformanceFee(null);
        musician.setAffiliatedNumber(0);
        musician.setWorks(null);
        musician.setAffiliated(false);
        musician.setClaims(null);

        Musician musicianDb = musicianRepository.save(musician);

        MusicianOutDto outDto = new MusicianOutDto();

        outDto.setFirstName(musicianDb.getFirstName());
        outDto.setLastName(musicianDb.getLastName());
        outDto.setDni(musicianDb.getDni());

        return outDto;
    }

    public ResponseEntity<List<Musician>> findAll(Float performanceFee, Boolean affiliated, LocalDate birthDate) {
        List<Musician> musician;

        musician = musicianRepository.findByFilters(performanceFee, affiliated, birthDate);
        return new ResponseEntity<>(musician, HttpStatus.OK);
    }

    public Musician findById(Long id) {
        Musician musician = musicianRepository.findById(id).orElseThrow(() -> new MusicianNotFoundException());

        return musician;
    }

    public MusicianOutDto findByIdV2(Long id) {
        Musician musician = musicianRepository.findById(id).orElseThrow(() -> new MusicianNotFoundException());

        MusicianOutDto outDto = new MusicianOutDto(musician.getFirstName(), musician.getLastName(), musician.getDni(), musician.getPerformanceFee(), musician.getAffiliated());

        return outDto;
    }


    public Musician update(long id, Musician updatedMusician) {
        Musician musician = musicianRepository.findById(id).orElseThrow(() -> new MusicianNotFoundException());

        musician.setFirstName(updatedMusician.getFirstName());
        musician.setLastName(updatedMusician.getLastName());
        musician.setBirthDate(updatedMusician.getBirthDate());
        musician.setAffiliated(updatedMusician.getAffiliated());
        musician.setDni(updatedMusician.getDni());
        musician.setAffiliatedNumber(updatedMusician.getAffiliatedNumber());

        musicianRepository.save(musician);
        return musician;
    }

    public MusicianOutDto updateV2(long id, MusicianUpdateConditionsDto updatedMusician) {
        Musician musician = musicianRepository.findById(id).orElseThrow(() -> new MusicianNotFoundException());

        musician.setAffiliated(updatedMusician.getAffiliated());
        musician.setPerformanceFee(updatedMusician.getPerformanceFee());

        Musician musicianDb = musicianRepository.save(musician);

        MusicianOutDto outDto = new MusicianOutDto();

        outDto.setFirstName(musicianDb.getFirstName());
        outDto.setLastName(musicianDb.getLastName());
        outDto.setDni(musicianDb.getDni());
        outDto.setPerformaceFee(musicianDb.getPerformanceFee());
        outDto.setAffiliated(musicianDb.getAffiliated());

        return outDto;
    }

    public void delete(long id) {
        if (!musicianRepository.existsById(id)) {
            throw new MusicianNotFoundException();
        }
        musicianRepository.deleteById(id);
    }

    public DeleteResponseDto deleteV2(long id) {
        musicianRepository.deleteById(id);

        DeleteResponseDto outDto = new DeleteResponseDto();
        outDto.setMessage("Músico borrado con exito");
        outDto.setId(id);
        return outDto;
    }
}

//EN ESTA CLASE PROGRAMO PARA LA BASE DE DATOS (CAPA LÓGICA DONDE, ES LO MÁS LIBRE)



