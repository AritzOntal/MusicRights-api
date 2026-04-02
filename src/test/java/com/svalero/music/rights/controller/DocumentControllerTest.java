/*
package com.svalero.music.rights.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.music.rights.domain.Claim;
import com.svalero.music.rights.domain.Document;
import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.domain.Work;
import com.svalero.music.rights.exception.*;
import com.svalero.music.rights.service.ClaimService;
import com.svalero.music.rights.service.DocumentService;
import com.svalero.music.rights.service.MusicianService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = DocumentController.class)
@Import(GlobalExceptionHandler.class)
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DocumentService documentService;
    @Autowired
    private ObjectMapper objectMapper;


    //                            "/documents" (GET)

    //404
    @Test
    void return404IfNotExistList() throws Exception {

        Mockito.when(documentService.findAll(
                Mockito.any(String.class),
                Mockito.any(Boolean.class),
                Mockito.any(LocalDate.class)
        )).thenThrow(new DocumentNotFoundException());

        mockMvc.perform(get("/documents")
                        .param("type", "1.0")
                        .param("complete", "true")
                        .param("createAd", LocalDate.now().toString()))
                .andExpect(status().isNotFound());
    }

    //400
    @Test
    void return400IfBadRquestPost() throws Exception {

        Document document = EntityTest.testDocument(false);

        mockMvc.perform(post("/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isBadRequest());
    }

    //200
    @Test
    void return200ifOk() throws Exception {

        Mockito.when(documentService.findAll(Mockito.any(String.class), Mockito.any(Boolean.class), Mockito.any(LocalDate.class)))
                .thenReturn(Mockito.mock(ResponseEntity.class));

        mockMvc.perform(get("/documents"))
                .andExpect(status().isOk());
    }


    //                        "/documents" (POST)
    //201
    @Test
    void returnOKifCreatedPost() throws Exception {

        Document document = EntityTest.testDocument(true);

        Mockito.when(documentService.add(document))
                .thenReturn(document);

        mockMvc.perform(post("/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isCreated());
    }

    //400
    @Test
    void returIfbadRquestPost() throws Exception {
        Document document = EntityTest.testDocument(false);

        mockMvc.perform(post("/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isBadRequest());
    }

    //404
    @Test
    void returIfNotExistWork() throws Exception {

        BodyForPerform accesBody = new BodyForPerform();
        String body = accesBody.getBodyDocument();

        Mockito.when(documentService.add(Mockito.any(Document.class)))
                .thenThrow(new DocumentNotFoundException());


        mockMvc.perform(post("/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());

    }


    //                     "/documents/{id}" (GET)

    //404
    @Test
    void return404IfNotExist() throws Exception {
        Long notexistId = 1L;

        //Le digo que si el controller llama con ese id, lance una Excepción
        Mockito.when(documentService.findById(notexistId))
                .thenThrow(new DocumentNotFoundException());

        //Y aquí lo provoco
        mockMvc.perform(get("/documents/{id}", notexistId))
                .andExpect(status().isNotFound()); //Lo eseperado es que de este error
    }

    //400
    @Test
    void return400IfIdIsInvalid() throws Exception {
        mockMvc.perform(get("/documents/abc")) // "abc" no puede convertir a Long y genera MethodArgument...
                .andExpect(status().isBadRequest());
        // Lo gestiona el @ControllerAdvice
    }


    //200

    @Test
    void returnOkIfnotProblem() throws Exception {
        Mockito.when(documentService.findById(Mockito.anyLong()))
                .thenReturn(Mockito.mock(Document.class));              //"mock" es para recibir un objeto de vuelta y "any" es para un enviar un argumento

        mockMvc.perform(get("/documents/{id}", 1L))
                .andExpect(status().isOk());
    }


    //                       "/documents/{id}" (PUT)

    //404
    @Test
    void return404IfIdIsInvalid() throws Exception {

        BodyForPerform accesBody = new BodyForPerform();
        String body = accesBody.getBodyDocument();

        Long notexistId = 1L;

        Mockito.when(documentService.edit(Mockito.eq(notexistId), Mockito.any(Document.class)))
                .thenThrow(new DocumentNotFoundException());

        mockMvc.perform(put("/documents/{id}", notexistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    //400
    @Test
    void return400IfBadRequest() throws Exception {

        Long notexistId = 1L;
        Document badRequest = EntityTest.testDocument(false);

        mockMvc.perform(put("/documents/{id}", notexistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    //200
    @Test
    void returnOKifNotProblem() throws Exception {

        Long idExist = 1L;
        Document goodRequest = EntityTest.testDocument(true);


        Mockito.when(documentService.edit(Mockito.any(Long.class), Mockito.any(Document.class)))
                .thenReturn(Mockito.mock(Document.class));

        mockMvc.perform(put("/documents/{id}", idExist)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goodRequest)))
                .andExpect(status().isOk());
    }

    //                      "/documents/{id}" (DELETE)

    //404
    @Test
    void return404IfIdNotExistOnDelete() throws Exception {
        Long notexistId = 1L;

        // musicianService.delete(id) es void → se mockea así:
        Mockito.doThrow(new DocumentNotFoundException())
                .when(documentService)
                .delete(notexistId);

        mockMvc.perform(delete("/documents/{id}", notexistId))
                .andExpect(status().isNotFound());
    }

    //204
    @Test
    void returnNotContetIfIdIsInvalid() throws Exception {
        Long existId = 1L;

        Mockito.doNothing().when(documentService).delete(existId);

        mockMvc.perform(delete("/documents/{id}", existId))
                .andExpect(status().isNoContent());
    }

    //400
    @Test
    void return400ifBadRequest() throws Exception {
        mockMvc.perform(delete("/documents/abc")) // "abc" no puede convertir a Long y genera MethodArgument...
                .andExpect(status().isBadRequest());
        // Lo gestiona el @ControllerAdvice
    }


    //200                       "concerts/by-musician/{id}" (GET)
    @Test
    void returnOkIfFinded() throws Exception {
        long id = 1L;

        Mockito.when(documentService.findByClaim(Mockito.anyLong()))
                .thenReturn(Mockito.mock(Document.class));

        mockMvc.perform(get("/documents/by-claim/{id}", id))
                .andExpect(status().isOk());
    }

    //404
    @Test
    void return400ifClaimNotFound() throws Exception {
        long noExistId = 1L;
        Mockito.when(documentService.findByClaim(Mockito.anyLong()))
                .thenThrow(new ClaimNotFoundException());

        mockMvc.perform(get("/documents/by-claim/{id}", noExistId))
                .andExpect(status().isNotFound());
    }

    //400
    @Test
    void return400ifBadRequestMusicianURL() throws Exception {
        mockMvc.perform(get("/documents/by-claim/abcc"))
                .andExpect(status().isBadRequest());
    }

}
*/
