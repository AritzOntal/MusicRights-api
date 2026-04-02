package com.svalero.music.rights.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.music.rights.domain.Claim;
import com.svalero.music.rights.domain.Musician;
import com.svalero.music.rights.exception.ClaimNotFoundException;
import com.svalero.music.rights.exception.GlobalExceptionHandler;
import com.svalero.music.rights.exception.MusicianNotFoundException;
import com.svalero.music.rights.service.ClaimService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClaimController.class)
@Import(GlobalExceptionHandler.class)
public class ClaimControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ClaimService claimService;


    //                            "/claims" (GET)

    //404
//    @Test
    void return404IfNotExistList() throws Exception {

        Mockito.when(claimService.findAll(
                Mockito.any(String.class),
                Mockito.any(String.class),
                Mockito.any(Boolean.class)
        )).thenThrow(new ClaimNotFoundException());

        mockMvc.perform(get("/claims")
                        .param("status", "hola")
                        .param("type", "quetal")
                        .param("pending", Boolean.FALSE.toString()))
                .andExpect(status().isNotFound());
    }

    //400
//    @Test
    void return400IfBadRquestPost() throws Exception {

        Claim claim = new Claim();
        claim.setId(1L);
        claim.setDescription("test");
        claim.setType("test");
        claim.setReference(null);
        claim.setStatus("open");
        claim.setPending(true);
        claim.setMusician(new Musician());

        //Aquí NO dependo del Service porque @Valid ya me lanza error 400.

        mockMvc.perform(post("/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claim))) //Lo provoco encajandole en JSON el objeto malo
                .andExpect(status().isBadRequest()); //Se espera respuesta 400
    }

    //200
//    @Test
    void return200ifOk() throws Exception {

        Mockito.when(claimService.findAll(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(Boolean.class)))
                .thenReturn(Mockito.mock(ResponseEntity.class));

        mockMvc.perform(get("/claims"))
                .andExpect(status().isOk());
    }


    //                        "/claims" (POST)
    //201
  //  @Test
    void returnOKifCreatedPost() throws Exception {

        Claim claim = new Claim();
        claim.setId(1L);
        claim.setDescription("test");
        claim.setType("test");
        claim.setReference("123423");
        claim.setStatus("open");
        claim.setPending(true);
        claim.setMusician(new Musician());

        Mockito.when(claimService.add(Mockito.any(Claim.class)))
                .thenReturn(claim);

        mockMvc.perform(post("/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claim)))
                .andExpect(status().isCreated());
    }

    //400
    //@Test
    void returIfbadRquestPost() throws Exception {

        Claim claim = new Claim();
        claim.setId(1L);
        claim.setDescription("test");
        claim.setType("test");
        claim.setReference(null);
        claim.setStatus("open");
        claim.setPending(true);
        claim.setMusician(new Musician());

        mockMvc.perform(post("/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claim)))
                .andExpect(status().isBadRequest());
    }

    //TODO ARREGLAR TEST
    //404
   // @Test
    void returIfNotExistMusician() throws Exception {

        BodyForPerform accesBody = new BodyForPerform();
        String body = accesBody.getBodyClaim();


        Mockito.when(claimService.add(Mockito.any(Claim.class)))
                .thenThrow(new MusicianNotFoundException());


        mockMvc.perform(post("/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());

    }


    //                     "/claims/{id}" (GET)

    //404
   // @Test
    void return404IfNotExist() throws Exception {
        Long notexistId = 1L;

        //Le digo que si el controller llama con ese id, lance una Excepción
        Mockito.when(claimService.findById(notexistId))
                .thenThrow(new ClaimNotFoundException());

        //Y aquí lo provoco
        mockMvc.perform(get("/claims/{id}", notexistId))
                .andExpect(status().isNotFound()); //Lo eseperado es que de este error
    }

    //400
  //  @Test
    void return400IfIdIsInvalid() throws Exception {
        mockMvc.perform(get("/claims/abc")) // "abc" no puede convertir a Long y genera MethodArgument...
                .andExpect(status().isBadRequest());
        // Lo gestiona el @ControllerAdvice
    }


    //200

   // @Test
    void returnOkIfnotProblem() throws Exception {
        Mockito.when(claimService.findById(Mockito.anyLong()))
                .thenReturn(Mockito.mock(Claim.class));              //"mock" es para recibir un objeto de vuelta y "any" es para un enviar un argumento

        mockMvc.perform(get("/claims/{id}", 1L))
                .andExpect(status().isOk());
    }


    //                       "/claims/{id}" (PUT)

    //404
  //  @Test
    void return404IfIdIsInvalid() throws Exception {

        BodyForPerform accesBody = new BodyForPerform();
        String body = accesBody.getBodyClaim();

        Long notexistId = 1L;

        Mockito.when(claimService.modify(Mockito.eq(notexistId), Mockito.any(Claim.class)))
                .thenThrow(new ClaimNotFoundException());

        mockMvc.perform(put("/claims/{id}", notexistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    //400
  //  @Test
    void return400IfBadRequest() throws Exception {

        Long notExistId = 1L;

        Claim claim = new Claim();

        claim.setId(notExistId);
        claim.setDescription("test");
        claim.setType("test");
        claim.setReference(null);
        claim.setStatus("open");
        claim.setPending(true);


        mockMvc.perform(put("/claims/{id}", notExistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claim)))
                .andExpect(status().isBadRequest());
    }

    //200
   // @Test
    void returnOKifNotProblem() throws Exception {

        Long idExist = 2L;

        Claim claim = new Claim();
        claim.setId(1L);
        claim.setDescription("test");
        claim.setType("test");
        claim.setReference("123423");
        claim.setStatus("open");
        claim.setPending(true);
        claim.setMusician(new Musician());

        Mockito.when(claimService.modify(Mockito.any(Long.class), Mockito.any(Claim.class)))
                .thenReturn(Mockito.mock(Claim.class));

        mockMvc.perform(put("/claims/{id}", idExist)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claim)))
                .andExpect(status().isOk());
    }

    //                      "/claims/{id}" (DELETE)

    //404
  //  @Test
    void return404IfIdNotExistOnDelete() throws Exception {
        Long notexistId = 1L;

        // musicianService.delete(id) es void → se mockea así:
        Mockito.doThrow(new ClaimNotFoundException())
                .when(claimService)
                .delete(notexistId);

        mockMvc.perform(delete("/claims/{id}", notexistId))
                .andExpect(status().isNotFound());
    }

    //204
  //  @Test
    void returnNotContetIfIdIsInvalid() throws Exception {
        Long existId = 1L;

        Mockito.doNothing().when(claimService).delete(existId);

        mockMvc.perform(delete("/claims/{id}", existId))
                .andExpect(status().isNoContent());
    }

    //400
  //  @Test
    void return400ifBadRequest() throws Exception {
        mockMvc.perform(delete("/claims/juan"))
                .andExpect(status().isBadRequest());
        // Lo gestiona el @ControllerAdvice
    }

}
