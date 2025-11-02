package com.empresa.transacciones.app.controllers;


import com.empresa.transacciones.app.dtos.ResponseStatus;
import com.empresa.transacciones.app.dtos.TransactionRequest;
import com.empresa.transacciones.app.dtos.TransactionResponse;
import com.empresa.transacciones.app.dtos.TransactionResponseAll;
import com.empresa.transacciones.app.services.TransactionService;
import com.empresa.transacciones.app.utils.commons.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    @DisplayName("create transaction ok")
    void createTransaction_success() throws Exception {
        TransactionResponse mockResponse = new TransactionResponse(
                new ResponseStatus("00", Constants.TRX_APROVED),
                "Aprobada",
                "111387"
        );

        Mockito.when(transactionService.createTransaction(any(TransactionRequest.class))).thenReturn(mockResponse);

        String requestJson = """
            {
              "identificador": "c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c",
              "numeroReferencia": "111387",
              "totalCompra": 120000,
              "direccion": "Calle 19 25 57"
            }
            """;

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultado.response").value("00"))
                .andExpect(jsonPath("$.resultado.message").value(Constants.TRX_APROVED))
                .andExpect(jsonPath("$.estadoTransaccion").value("Aprobada"))
                .andExpect(jsonPath("$.numeroReferencia").value("111387"));
    }


    @Test
    @DisplayName("cancel transaction ok")
    void cancelTransaction_success() throws Exception {
        TransactionResponse mockResponse = new TransactionResponse(
                new ResponseStatus("00", Constants.CANCELED_PURCHASE),
                "Anulada",
                "111387"
        );

        Mockito.when(transactionService.cancelTransaction(any(TransactionRequest.class))).thenReturn(mockResponse);

        String requestJson = """
            {
              "identificador": "c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c",
              "numeroReferencia": "111387",
              "totalCompra": 120000
            }
            """;

        mockMvc.perform(post("/api/v1/transactions/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultado.response").value("00"))
                .andExpect(jsonPath("$.resultado.message").value(Constants.CANCELED_PURCHASE))
                .andExpect(jsonPath("$.numeroReferencia").value("111387"));
    }

    @Test
    @DisplayName("create transaction fails - missing field")
    void createTransaction_missingField() throws Exception {
        String invalidRequest = """
            {
              "numeroReferencia": "REF12345",
              "direccionCompra": "Calle 123"
            }
            """; 

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("should return all transactions successfully")
    void getAllTransactions_success() throws Exception {
        List<TransactionResponseAll> mockResponse = List.of(
                new TransactionResponseAll(
                        "023458",
                        new BigDecimal( "6500000"),
                        "Rechazada",
                        LocalDateTime.now(),
                        null
                ),
                new TransactionResponseAll(
                        "123456",
                        new BigDecimal( "6500000"),
                        "Aprobada",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(3)
                )
        );

        Mockito.when(transactionService.getAllTransactions()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroReferencia").value("023458"))
                .andExpect(jsonPath("$[0].estadoTransaccion").value("Rechazada"))
                .andExpect(jsonPath("$[1].numeroReferencia").value("123456"))
                .andExpect(jsonPath("$[1].estadoTransaccion").value("Aprobada"));
    }

}
