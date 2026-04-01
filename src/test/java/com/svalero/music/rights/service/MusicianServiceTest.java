package com.svalero.music.rights.service;


import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.domain.Work;
import com.svalero.music.rights.exception.MusicianNotFoundException;
import com.svalero.music.rights.exception.WorkNotFoundException;
import com.svalero.music.rights.repository.MusicianRepository;
import com.svalero.music.rights.repository.WorkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MusicianServiceTest {

    @InjectMocks // Crea instancia real he inyecta los mocks
    private MusicianService musicianService;
    @Mock //esto es un mock para lo que yo configure con "when..."
    private MusicianRepository musicianRepository;
    @Mock
    private WorkRepository workRepository;


    //FIND ALL

    @Test
    void findAllOK() {
        List<Musician> musiciansList = List.of(
                new Musician(null, "Juan", "Jimenez", LocalDate.of(1980, 6, 12), true, "12345678A", 250.5f, 126484572L, List.of(), List.of()),
                new Musician(null, "Maria", "Lopez", LocalDate.of(1992, 11, 2), false, "87654321B", 310.75f, 984531256L, List.of(), List.of())
        );

        when(musicianRepository.findByFilters(null, null, null)).thenReturn(musiciansList);

        ResponseEntity<List<Musician>> musiciansMocked = musicianService.findAll(null, null, null);

        List<Musician> result = musiciansMocked.getBody(); //Al devolver ResponseEntity recojo primerop el cuerpo de la respuesta.
        assertEquals(2, result.size());

    }

    //ADD

    @Test
    void testAddOk() {
        Musician musicianMock = new Musician(null, "Juan", "Jimenez", LocalDate.of(1980, 6, 12), true, "12345678A", 250.5f, 126484572L, List.of(), List.of());

        //Antes de hacer nada Mockear de quien depende y simular comportamiento (Repositorio)
        when(musicianRepository.save(any(Musician.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); //esto devuelve lo mismo que añade

        //LLamo de verdad al metodo add en Service y guardo el resultado
        Musician result = musicianService.add(musicianMock);
        //Cmparo resultados (tiene que el mismo que meto)
        assertSame(musicianMock, result);
        //Verificar que se llamo al Respositorio
        verify(musicianRepository).save(musicianMock);

    }

    @Test
    void testAddWorkNotFound() {
        Long noExistingId = 1L;
        Musician musician = new Musician();
        Work workMock = new Work();
        workMock.setId(noExistingId);
        List<Work> workList = List.of(workMock);
        musician.setWorks(workList);

        when(workRepository.findById(noExistingId))
                .thenReturn(Optional.empty());

        assertThrows(WorkNotFoundException.class,
                () -> musicianService.add(musician));
    }

    //FIND BY ID

    @Test
    void testFindByIdOk() {

        Long existingId = 1L;
        Musician musicianMock = new Musician();
        musicianMock.setId(existingId);

        when(musicianRepository.findById(existingId))
                .thenReturn(Optional.of(musicianMock));

        Musician result = musicianService.findById(existingId);
        assertSame(musicianMock, result);
    }

    @Test
    void testFindByIdException() {
        Long notExistingId = 99L;

        when(musicianRepository.findById(notExistingId))
                .thenReturn(Optional.empty());

        assertThrows(MusicianNotFoundException.class,
                () -> musicianService.findById(notExistingId));
    }

    //EDIT

    @Test
    void testEditOK() {
        Long existingId = 1L;
        Musician musicianDb = new Musician();
        musicianDb.setId(existingId);

        Musician newMusician = new Musician();
        newMusician.setFirstName("Aritz");

        when(musicianRepository.findById(existingId))
                .thenReturn(Optional.of(musicianDb));

        when(musicianRepository.save(any(Musician.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        //La lllamada de verdad
        Musician result = musicianService.update(existingId, newMusician);
        assertSame(musicianDb, result);
    }

    @Test
    void testEditNotFound() {
        Long noExist = 1L;
        Musician musicianDb = new Musician();
        musicianDb.setId(noExist);

        Musician newMusician = new Musician();
        newMusician.setFirstName("Aritz");

        when(musicianRepository.findById(noExist))
                .thenThrow(MusicianNotFoundException.class);

        assertThrows(MusicianNotFoundException.class,
                () -> musicianService.update(noExist, newMusician));
    }


    //DELETE

    @Test
    void testDeleteOk() {
        Long existId = 1L;
        Musician musicianDb = new Musician();
        musicianDb.setId(existId);

        doNothing().when(musicianRepository).deleteById(existId);

        musicianService.delete(existId);

        verify(musicianRepository).deleteById(existId);

    }

    @Test
    void testDeleteNotFound() {
        Long noExistingId = 1L;

        doThrow(MusicianNotFoundException.class).when(musicianRepository).deleteById(noExistingId);

        assertThrows(MusicianNotFoundException.class,
                () -> musicianService.delete(noExistingId));

        verify(musicianRepository).deleteById(noExistingId);
    }
}
