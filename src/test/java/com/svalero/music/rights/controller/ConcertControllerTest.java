package com.svalero.music.rights.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.music.rights.domain.Concert;
import com.svalero.music.rights.exception.*;
import com.svalero.music.rights.service.ConcertService;
import com.svalero.music.rights.util.BodyForPerform;
import com.svalero.music.rights.util.EntityTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ConcertController.class)
@Import(GlobalExceptionHandler.class)
public class ConcertControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ConcertService concertService;


    //                            "/concerts" (GET)

    //404
//    @Test
    void return404IfNotExistList() throws Exception {

        Mockito.when(concertService.findAll(
                Mockito.any(String.class),
                Mockito.any(String.class),
                Mockito.any(Boolean.class)
        )).thenThrow(new ConcertNotFoundException());

        mockMvc.perform(get("/concerts")
                        .param("city", "hola")
                        .param("status", "quetal")
                        .param("performed", Boolean.FALSE.toString()))
                .andExpect(status().isNotFound());
    }

    //400
  //  @Test
    void return400IfBadRquestPost() throws Exception {

        Concert concert = EntityTest.testConcert(false);

        mockMvc.perform(post("/concerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concert)))
                .andExpect(status().isBadRequest());
    }

    //200
   // @Test
    void return200ifOk() throws Exception {

        Mockito.when(concertService.findAll(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(Boolean.class)))
                .thenReturn(Mockito.mock(ResponseEntity.class));

        mockMvc.perform(get("/concerts"))
                .andExpect(status().isOk());
    }


    //                        "/concerts" (POST)
    //201
   // @Test
    void returnOKifCreatedPost() throws Exception {

        Concert concert = EntityTest.testConcert(true);

        Mockito.when(concertService.add(Mockito.any(Concert.class)))
                .thenReturn(concert);

        mockMvc.perform(post("/concerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concert)))
                .andExpect(status().isCreated());
    }

    //400
  //  @Test
    void returIfbadRquestPost() throws Exception {

        Concert concert = EntityTest.testConcert(false);

        mockMvc.perform(post("/concerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concert)))
                .andExpect(status().isBadRequest());
    }

    //TODO ARREGLAR TEST
    //404
   // @Test
    void returIfNotExistMusician() throws Exception {

        BodyForPerform accesBody = new BodyForPerform();
        String body = accesBody.getBodyConcert();


        Mockito.when(concertService.add(Mockito.any(Concert.class)))
                .thenThrow(new MusicianNotFoundException());


        mockMvc.perform(post("/concerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))  //EN EL BODY LE PASO POR JSON UN MUSICO QUE NO EXISTE
                .andExpect(status().isNotFound());

    }

    //                     "/musicians/{id}" (GET)

    //404
   // @Test
    void return404IfNotExist() throws Exception {
        long notexistId = 1L;

        //Le digo que si el controller llama con ese id (me lo invento), lance una Excepción
        Mockito.when(concertService.findById(notexistId))
                .thenThrow(new MusicianNotFoundException());

        //Y aquí lo provoco
        mockMvc.perform(get("/concerts/{id}", notexistId))
                .andExpect(status().isNotFound()); //Lo eseperado es que de este error
    }

    //400
  //  @Test
    void return400IfIdIsInvalid() throws Exception {
        String invalidId = "invalid";

        mockMvc.perform(get("/concerts/{id}", invalidId)) // "abc" no puede convertir a Long y genera MethodArgument...
                .andExpect(status().isBadRequest());
        // Lo gestiona el @ControllerAdvice
    }


    //200

  //  @Test
    void returnOkIfnotProblem() throws Exception {
        Mockito.when(concertService.findById(Mockito.anyLong()))
                .thenReturn(Mockito.mock(Concert.class));              //"mock" es para recibir un objeto de vuelta y "any" es para un enviar un argumento

        mockMvc.perform(get("/concerts/{id}", 1L))
                .andExpect(status().isOk());
    }

    //                       "/concerts/{id}" (PUT)

    //404
  //  @Test
    void return404IfIdIsInvalid() throws Exception {

        BodyForPerform accesBody = new BodyForPerform();
        String body = accesBody.getBodyConcert();

        long notexistId = 1L;

        Mockito.when(concertService.edit(Mockito.eq(notexistId), Mockito.any(Concert.class)))
                .thenThrow(new ConcertNotFoundException());

        mockMvc.perform(put("/concerts/{id}", notexistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    //400
   // @Test
    void return400IfBadRequest() throws Exception {

        long notExistId = 1L;

        Concert concert = EntityTest.testConcert(false);

        mockMvc.perform(put("/concerts/{id}", notExistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concert)))
                .andExpect(status().isBadRequest());
    }

    //200
//    @Test
    void returnOKifNotProblem() throws Exception {

        long idExist = 1L;

        Concert concert = EntityTest.testConcert(true);

        Mockito.when(concertService.edit(Mockito.any(Long.class), Mockito.any(Concert.class)))
                .thenReturn(Mockito.mock(Concert.class));

        mockMvc.perform(put("/concerts/{id}", idExist)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concert)))
                .andExpect(status().isOk());
    }

    //                      "/musicians/{id}" (DELETE)

    //404
  //  @Test
    void return404IfIdNotExistOnDeleted() throws Exception {
        long notexistId = 1L;

        Mockito.doThrow(new ConcertNotFoundException())
                .when(concertService)
                .delete(notexistId);

        mockMvc.perform(delete("/concerts/{id}", notexistId))
                .andExpect(status().isNotFound());
    }

    //204
  //  @Test
    void returnNotContentIfDeleted() throws Exception {
        long existId = 1L;

        Mockito.doNothing().when(concertService).delete(existId);

        mockMvc.perform(delete("/concerts/{id}", existId))
                .andExpect(status().isNoContent());
    }

    //400
  //  @Test
    void return400ifBadRequest() throws Exception {
        mockMvc.perform(delete("/concerts/zzz"))
                .andExpect(status().isBadRequest());
        // Lo gestiona el @ControllerAdvice
    }

    //200                       "concerts/by-musician/{id}" (GET)

   // @Test
    void returnOkIfFinded() throws Exception {
        long id = 1L;

        Mockito.when(concertService.findAllbyMusicianId(Mockito.anyLong()))
                .thenReturn(new ArrayList<>()); //COMO ES UNA LISTA DE TIPOS DEVOLVERA ARRAYLIST SIN MÁS

        mockMvc.perform(get("/concerts/by-musician/{id}", id))
                .andExpect(status().isOk());
    }

    //404
 //   @Test
    void return400ifMusicianNotFound() throws Exception {
        long noExistId = 1L;
        Mockito.when(concertService.findAllbyMusicianId(Mockito.anyLong()))
                .thenThrow(new MusicianNotFoundException());

        mockMvc.perform(get("/concerts/by-musician/{id}", noExistId))
                .andExpect(status().isNotFound());
    }

    //400
   // @Test
    void return400ifBadRequestMusicianURL() throws Exception {
        mockMvc.perform(get("/concerts/by-musician/zzz"))
                .andExpect(status().isBadRequest());
    }

}
