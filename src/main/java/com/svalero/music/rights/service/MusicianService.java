package com.svalero.music.rights.service;


import com.svalero.music.rights.domain.Claim;
import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.domain.Work;
import com.svalero.music.rights.dtos.MusicianOutDto;
import com.svalero.music.rights.exception.ClaimNotFoundException;
import com.svalero.music.rights.exception.MusicianNotFoundException;
import com.svalero.music.rights.exception.WorkNotFoundException;
import com.svalero.music.rights.repository.MusicianRepository;
import com.svalero.music.rights.repository.WorkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicianService {

    private MusicianRepository musicianRepository;
    private WorkRepository workRepository; //REFERENCIA AL REPOSITORIO POR AUTOWIRED

    public MusicianService(MusicianRepository musicianRepository, WorkRepository workRepository) { //MEJOR CON CONSTRUCTOR QUE CON AUTOWIRED
        this.musicianRepository = musicianRepository;
        this.workRepository = workRepository;
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

    public ResponseEntity<List<Musician>> findAll(Float performanceFee, Boolean affiliated, LocalDate birthDate) {
        List<Musician> musician;

        musician = musicianRepository.findByFilters(performanceFee, affiliated, birthDate);
        return new ResponseEntity<>(musician, HttpStatus.OK);
    }

    public Musician findById(Long id) {
        Musician musician = musicianRepository.findById(id)
                .orElseThrow(() -> new MusicianNotFoundException());

        return musician;
    }

    public MusicianOutDto findByIdV2(Long id) {
        Musician musician = musicianRepository.findById(id)
                .orElseThrow(() -> new MusicianNotFoundException());

        MusicianOutDto outDto = new MusicianOutDto(
                musician.getFirstName(),
                musician.getLastName(),
                musician.getDni()
        );

        return outDto;
    }


    public Musician edit(long id, Musician updatedMusician) {
        Musician musician = musicianRepository.findById(id)
                .orElseThrow(() -> new MusicianNotFoundException());

        musician.setFirstName(updatedMusician.getFirstName());
        musician.setLastName(updatedMusician.getLastName());
        musician.setBirthDate(updatedMusician.getBirthDate());
        musician.setAffiliated(updatedMusician.getAffiliated());
        musician.setDni(updatedMusician.getDni());
        musician.setAffiliatedNumber(updatedMusician.getAffiliatedNumber());

        musicianRepository.save(musician);
        return musician;
    }

    public void delete(long id) {
        musicianRepository.deleteById(id);
    }

}

//EN ESTA CLASE PROGRAMO PARA LA BASE DE DATOS (CAPA LÓGICA DONDE, ES LO MÁS LIBRE)



