/*
package com.svalero.music.rights.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.music.rights.domain.Claim;
import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.domain.Work;
import com.svalero.music.rights.exception.GlobalExceptionHandler;
import com.svalero.music.rights.exception.MusicianNotFoundException;
import com.svalero.music.rights.exception.WorkNotFoundException;
import com.svalero.music.rights.service.MusicianService;
import com.svalero.music.rights.util.BodyForPerform;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MusicianController.class)
@Import(GlobalExceptionHandler.class)
public class MusicianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicianService musicianService;
    @Autowired
    private ObjectMapper objectMapper;


    //                            "/musicians" (GET)

    //404
    @Test
    void return404IfNotExistList() throws Exception {

        Mockito.when(musicianService.findAll(
                Mockito.any(Float.class),
                Mockito.any(Boolean.class),
                Mockito.any(LocalDate.class)
        )).thenThrow(new MusicianNotFoundException()); //any se usa para decirle "cualquier cosa de la Clase X

        mockMvc.perform(get("/musicians")
                        .param("performanceFee", "1.0")
                        .param("affiliated", "true")
                        .param("birthDate", LocalDate.now().toString()))
                .andExpect(status().isNotFound());
    }

    //400
    @Test
    void return400IfBadRquestPost() throws Exception {

        List<Work> works = new ArrayList<>();
        List<Claim> claims = new ArrayList<>();
        LocalDate birthDate = LocalDate.of(1992, 1, 1);

        Musician musician = new Musician();
        musician.setFirstName("");
        musician.setLastName("Sánchez");
        musician.setBirthDate(birthDate);
        musician.setAffiliated(true);
        musician.setDni("12345"); //ERRRO PROVOCADO
        musician.setPerformanceFee(2.6f);
        musician.setAffiliatedNumber(12312L);
        musician.setWorks(works);
        musician.setClaims(claims);

        //Aquí NO dependo del Service porque @Valid ya me lanza error 400.

        mockMvc.perform(post("/musicians")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(musician))) //Lo provoco encajandole en JSON el objeto malo
                .andExpect(status().isBadRequest()); //Se espera respuesta 400
    }

    //200
    @Test
    void return200ifOk() throws Exception {

        Mockito.when(musicianService.findAll(Mockito.any(Float.class), Mockito.any(Boolean.class), Mockito.any(LocalDate.class)))
                .thenReturn(Mockito.mock(ResponseEntity.class));

        mockMvc.perform(get("/musicians"))
                .andExpect(status().isOk());
    }


    //                        "/musicians" (POST)
    //201
    @Test
    void returnOKifCreatedPost() throws Exception {

        List<Work> works = new ArrayList<>();
        List<Claim> claims = new ArrayList<>();
        LocalDate birthDate = LocalDate.of(1992, 1, 1);

        Musician musician = new Musician();
        musician.setFirstName("Pedro");
        musician.setLastName("Sánchez");
        musician.setBirthDate(birthDate);
        musician.setAffiliated(true);
        musician.setDni("45916040J");
        musician.setPerformanceFee(2.6f);
        musician.setAffiliatedNumber(12312L);
        musician.setWorks(works);
        musician.setClaims(claims);

        Mockito.when(musicianService.add(Mockito.any(Musician.class)))
                .thenReturn(musician);

        mockMvc.perform(post("/musicians")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(musician)))
                .andExpect(status().isCreated());
    }

    //400
    @Test
    void returIfbadRquestPost() throws Exception {
        List<Work> works = new ArrayList<>();
        List<Claim> claims = new ArrayList<>();
        LocalDate birthDate = LocalDate.of(1992, 1, 1);

        Musician musician = new Musician();
        musician.setFirstName("Pedro");
        musician.setLastName("Sánchez");
        musician.setBirthDate(birthDate);
        musician.setAffiliated(true);
        musician.setDni("459898fffZ");
        musician.setPerformanceFee(2.6f);
        musician.setAffiliatedNumber(12312L);
        musician.setWorks(works);
        musician.setClaims(claims);

        mockMvc.perform(post("/musicians")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(musician)))
                .andExpect(status().isBadRequest());
    }

    //404
    @Test
    void returIfNotExistWork() throws Exception {

        BodyForPerform accesBody = new BodyForPerform();
        String body = accesBody.getBodyMusician();


        Mockito.when(musicianService.add(Mockito.any(Musician.class)))
                .thenThrow(new WorkNotFoundException());


        mockMvc.perform(post("/musicians")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());

    }


    //                     "/musicians/{id}" (GET)

    //404
    @Test
    void return404IfNotExist() throws Exception {
        Long notexistId = 1L;

        //Le digo que si el controller llama con ese id, lance una Excepción
        Mockito.when(musicianService.findById(notexistId))
                .thenThrow(new MusicianNotFoundException());

        //Y aquí lo provoco
        mockMvc.perform(get("/musicians/{id}", notexistId))
                .andExpect(status().isNotFound()); //Lo eseperado es que de este error
    }

    //400
    @Test
    void return400IfIdIsInvalid() throws Exception {
        mockMvc.perform(get("/musicians/abc")) // "abc" no puede convertir a Long y genera MethodArgument...
                .andExpect(status().isBadRequest());
        // Lo gestiona el @ControllerAdvice
    }


    //200

    @Test
    void returnOkIfnotProblem() throws Exception {
        Mockito.when(musicianService.findById(Mockito.anyLong()))
                .thenReturn(Mockito.mock(Musician.class));              //"mock" es para recibir un objeto de vuelta y "any" es para un enviar un argumento

        mockMvc.perform(get("/musicians/{id}", 1L))
                .andExpect(status().isOk());
    }


    //                       "/musicians/{id}" (PUT)

    //404
    @Test
    void return404IfIdIsInvalid() throws Exception {

        BodyForPerform accesBody = new BodyForPerform();
        String body = accesBody.getBodyMusician();

        Long notexistId = 1L;

        Mockito.when(musicianService.update(Mockito.eq(notexistId), Mockito.any(Musician.class)))
                .thenThrow(new MusicianNotFoundException());

        mockMvc.perform(put("/musicians/{id}", notexistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    //400
    @Test
    void return400IfBadRequest() throws Exception {

        List<Work> works = new ArrayList<>();
        List<Claim> claims = new ArrayList<>();
        LocalDate birthDate = LocalDate.of(1992, 1, 1);
        Long notexistId = 1L;

        Musician musician = new Musician();
        musician.setFirstName("Pedro");
        musician.setLastName("Sanchez");
        musician.setBirthDate(null);
        musician.setAffiliated(false);
        musician.setDni("45916040j2323");
        musician.setPerformanceFee(2.6f);
        musician.setAffiliatedNumber(12312L);
        musician.setWorks(works);
        musician.setClaims(claims);

        mockMvc.perform(put("/musicians/{id}", notexistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(musician)))
                .andExpect(status().isBadRequest());
    }

    //200
    @Test
    void returnOKifNotProblem() throws Exception {

        List<Work> works = new ArrayList<>();
        List<Claim> claims = new ArrayList<>();
        LocalDate birthDate = LocalDate.of(1992, 1, 1);
        Long idExist = 1L;

        Musician musician = new Musician();
        musician.setFirstName("Pedro");
        musician.setLastName("Sanchez");
        musician.setBirthDate(birthDate);
        musician.setAffiliated(true);
        musician.setDni("45916040J");
        musician.setPerformanceFee(2.6f);
        musician.setAffiliatedNumber(12312L);
        musician.setWorks(works);
        musician.setClaims(claims);

        Mockito.when(musicianService.update(Mockito.any(Long.class), Mockito.any(Musician.class)))
                .thenReturn(Mockito.mock(Musician.class));

        mockMvc.perform(put("/musicians/{id}", idExist)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(musician)))
                .andExpect(status().isOk());
    }

    //                      "/musicians/{id}" (DELETE)

    //404
    @Test
    void return404IfIdNotExistOnDelete() throws Exception {
        Long notexistId = 1L;

        // musicianService.delete(id) es void → se mockea así:
        Mockito.doThrow(new MusicianNotFoundException())
                .when(musicianService)
                .delete(notexistId);

        mockMvc.perform(delete("/musicians/{id}", notexistId))
                .andExpect(status().isNotFound());
    }

    //204
    @Test
    void returnNotContetIfIdIsInvalid() throws Exception {
        Long existId = 1L;

        Mockito.doNothing().when(musicianService).delete(existId);

        mockMvc.perform(delete("/musicians/{id}", existId))
                .andExpect(status().isNoContent());
    }

    //400
    @Test
    void return400ifBadRequest() throws Exception {
        mockMvc.perform(delete("/musicians/pedro")) // "abc" no puede convertir a Long y genera MethodArgument...
                .andExpect(status().isBadRequest());
        // Lo gestiona el @ControllerAdvice
    }
}

*/
