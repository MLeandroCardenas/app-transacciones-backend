package com.empresa.transacciones.app.controllers;


import com.empresa.transacciones.app.dtos.*;
import com.empresa.transacciones.app.services.CardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @Test
    @DisplayName("create card ok")
    void createCard_success() throws Exception {
        CardResponse mockResponse = new CardResponse(
                new ResponseStatus("00", "Éxito"),
                3,
                "569823****0156",
                "c59e240cead5ea3133d1ba98ec3e92396d82044c42dccf571f3c7cc1e603fe6c"
        );
        Mockito.when(cardService.createCard(any())).thenReturn(mockResponse);

        String requestJson = """
            {
              "pan": 9999999999999999,
              "titular": "Michael",
              "cedula": "25897412698",
              "tipo": "C",
              "phone": 1234567895
            }
            """;

        mockMvc.perform(post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status.response").value("00"))
                .andExpect(jsonPath("$.status.message").value("Éxito"))
                .andExpect(jsonPath("$.identificador").value("c59e240cead5ea3133d1ba98ec3e92396d82044c42dccf571f3c7cc1e603fe6c"));
    }


    @Test
    @DisplayName("Show enroll a card")
    void cardEnroll_success() throws Exception {
        EnrollResponse mockResponse = new EnrollResponse(
                new ResponseStatus("00", "Éxito"),
                "9999999999999999"
        );
        Mockito.when(cardService.enrollCard(any())).thenReturn(mockResponse);

        String requestJson = """
            {
              "identificador": "7149f9d1a24d92ac8b508c73b5b6cc6e1913fa336d5dae83e397826a7ffb82fd",
              "numeroValidacion": 123
            }
            """;

        mockMvc.perform(post("/api/v1/cards/enrolar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.response").value("00"))
                .andExpect(jsonPath("$.status.message").value("Éxito"))
                .andExpect(jsonPath("$.pan").value("9999999999999999"));
    }


    @Test
    @DisplayName("Should card by identifier")
    void getCard_success() throws Exception {
        CardConsultResponse mockResponse = new CardConsultResponse("156478987545", "Michael", "25897412698", new BigDecimal("1234567895"), "ENROLADA");
        Mockito.when(cardService.getCard("ABC123")).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(get("/api/v1/cards/card")
                        .param("identificador", "ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titular").value("Michael"))
                .andExpect(jsonPath("$.estado").value("ENROLADA"));
    }

    @Test
    @DisplayName("show delete card ok")
    void deleteCard_success() throws Exception {
        Mockito.doNothing().when(cardService).deleteCard(any(CardDeleteRequest.class));

        String requestJson = """
            {
              "identificador": "40ba2adce2b4213f2dddf3b5a2f9860b3f1262d92ec987921ece90f087968ea0"
            }
            """;

        mockMvc.perform(put("/api/v1/cards/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("00"))
                .andExpect(jsonPath("$.message").value("Se ha eliminado la tarjeta"));
    }
    
    @Test
    @DisplayName("Show all cards ok")
    void getAllCards_success() throws Exception {
        // Arrange
        List<CardResponseAll> mockCards = List.of(
                new CardResponseAll("156478****7545", "C", "ENROLADA", 9,
                        "c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c"),
                new CardResponseAll("1545", "C", "INACTIVA", 56,
                        "40ba2adce2b4213f2dddf3b5a2f9860b3f1262d92ec987921ece90f087968ea0")
        );

        Mockito.when(cardService.getAllCards()).thenReturn(mockCards);

        // Act & Assert
        mockMvc.perform(get("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].pan").value("156478****7545"))
                .andExpect(jsonPath("$[0].tipo").value("C"))
                .andExpect(jsonPath("$[0].estado").value("ENROLADA"))
                .andExpect(jsonPath("$[0].numeroValidacion").value(9))
                .andExpect(jsonPath("$[1].estado").value("INACTIVA"))
                .andExpect(jsonPath("$[1].tipo").value("C"))
                .andExpect(jsonPath("$[1].numeroValidacion").value(56));
    }



}

