package com.empresa.transacciones.app.controllers;


import com.empresa.transacciones.app.dtos.ResponseStatus;
import com.empresa.transacciones.app.dtos.TransactionRequest;
import com.empresa.transacciones.app.dtos.TransactionResponse;
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
}
