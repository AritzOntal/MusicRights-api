/*
package com.svalero.music.rights.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.music.rights.domain.Work;
import com.svalero.music.rights.exception.GlobalExceptionHandler;
import com.svalero.music.rights.exception.MusicianNotFoundException;
import com.svalero.music.rights.exception.WorkNotFoundException;
import com.svalero.music.rights.service.MusicianService;
import com.svalero.music.rights.service.WorkService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WorkController.class)
@Import(GlobalExceptionHandler.class)
public class WorkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkService workService;
    @Autowired
    private ObjectMapper objectMapper;


    //                            "/works" (GET)

    //404
    @Test
    void return404IfNotExistList() throws Exception {

        Mockito.when(workService.findAll(
                Mockito.any(Float.class),
                Mockito.any(LocalDate.class),
                Mockito.any(Boolean.class)
        )).thenThrow(new WorkNotFoundException());

        mockMvc.perform(get("/works")
                        .param("duration", "1.0")
                        .param("composedAt", "2023-05-06")
                        .param("registred", "true"))
                .andExpect(status().isNotFound());
    }

    //400
    @Test
    void return400IfBadRquestPost() throws Exception {

        Work work = EntityTest.testWork(false);

        //Aquí NO dependo del Service porque @Valid ya me lanza error 400.

        mockMvc.perform(post("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(work))) //Lo provoco encajandole en JSON el objeto malo
                .andExpect(status().isBadRequest()); //Se espera respuesta 400
    }

    //200
    @Test
    void return200ifOk() throws Exception {

        Mockito.when(workService.findAll(Mockito.any(Float.class), Mockito.any(LocalDate.class), Mockito.any(Boolean.class)))
                .thenReturn(Mockito.mock(ResponseEntity.class));

        mockMvc.perform(get("/works"))
                .andExpect(status().isOk());
    }


    //                        "/works" (POST)
    //201
    @Test
    void returnOKifCreatedPost() throws Exception {
        Work work = EntityTest.testWork(true);

        Mockito.when(workService.add(Mockito.any(Work.class)))
                .thenReturn(work);

        mockMvc.perform(post("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(work)))
                .andExpect(status().isCreated());
    }

    //400
    @Test
    void returIfbadRquestPost() throws Exception {
        Work work = EntityTest.testWork(false);

        mockMvc.perform(post("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(work)))
                .andExpect(status().isBadRequest());
    }

    //404
    @Test
    void returIfNotExistWork() throws Exception {

        BodyForPerform accesBody = new BodyForPerform();
        String body = accesBody.getBodyWork();


        Mockito.when(workService.add(Mockito.any(Work.class)))
                .thenThrow(new WorkNotFoundException());


        mockMvc.perform(post("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());

    }


    //                     "/works/{id}" (GET)

    //404
    @Test
    void return404IfNotExist() throws Exception {
        Long notexistId = 1L;

        Mockito.when(workService.findById(notexistId))
                .thenThrow(new WorkNotFoundException());

        mockMvc.perform(get("/works/{id}", notexistId))
                .andExpect(status().isNotFound());
    }

    //400
    @Test
    void return400IfIdIsInvalid() throws Exception {
        mockMvc.perform(get("/works/abc")) // "abc" no puede convertir a Long y genera MethodArgument...
                .andExpect(status().isBadRequest());
        // Lo gestiona el @ControllerAdvice
    }


    //200

    @Test
    void returnOkIfnotProblem() throws Exception {
        Long existisId = 1L;

        Mockito.when(workService.findById(Mockito.anyLong()))
                .thenReturn(Mockito.mock(Work.class));              //"mock" es para recibir un objeto de vuelta y "any" es para un enviar un argumento

        mockMvc.perform(get("/works/{id}", existisId))
                .andExpect(status().isOk());
    }


    //                       "/musicians/{id}" (PUT)

    //404
    @Test
    void return404IfIdIsInvalid() throws Exception {

        BodyForPerform accesBody = new BodyForPerform();
        String body = accesBody.getBodyWork();

        Long notexistId = 1L;

        Mockito.when(workService.edit(Mockito.any(Long.class), Mockito.any(Work.class)))
                .thenThrow(new WorkNotFoundException());

        mockMvc.perform(put("/works/{id}", notexistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    //400
    @Test
    void return400IfBadRequest() throws Exception {
        Work work = EntityTest.testWork(false);

        mockMvc.perform(put("/works/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(work)))
                .andExpect(status().isBadRequest());
    }

    //200
    @Test
    void returnOKifNotProblem() throws Exception {
        Work work = EntityTest.testWork(true);
        Long existisId = 1L;

        Mockito.when(workService.edit(Mockito.any(Long.class), Mockito.any(Work.class)))
                .thenReturn(Mockito.mock(ResponseEntity.class));

        mockMvc.perform(put("/works/{id}", existisId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(work)))
                .andExpect(status().isOk());
    }

    //                      "/musicians/{id}" (DELETE)

    //404
    @Test
    void return404IfIdNotExistOnDelete() throws Exception {
        Long notexistId = 1L;

        Mockito.doThrow(new WorkNotFoundException())
                .when(workService)
                .delete(notexistId);

        mockMvc.perform(delete("/works/{id}", notexistId))
                .andExpect(status().isNotFound());
    }

    //204
    @Test
    void returnNotContetIfIdIsInvalid() throws Exception {
        Long existId = 1L;

        Mockito.doNothing().when(workService).delete(existId);

        mockMvc.perform(delete("/works/{id}", existId))
                .andExpect(status().isNoContent());
    }

    //400
    @Test
    void return400ifBadRequest() throws Exception {
        mockMvc.perform(delete("/works/abc"))
                .andExpect(status().isBadRequest());
    }
}
*/
