package com.empresa.transacciones.app.mappers;

import com.empresa.transacciones.app.dtos.*;
import com.empresa.transacciones.app.models.Card;
import com.empresa.transacciones.app.utils.enums.CardTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CardMapperTest {

    private CardMapper cardMapper;

    @BeforeEach
    void setUp() {
        cardMapper = new CardMapper();
    }

    @Test
    @DisplayName("Debe mapear correctamente de CardRequest a Card")
    void testToEntity() {
        CardRequest request = new CardRequest(
                new BigDecimal("156478987545"),
                "Michael",
                "123456789",
                CardTypeEnum.CREDITO,
                new BigDecimal("1235898745")
        );

        Card card = cardMapper.toEntity(request);

        assertNotNull(card);
        assertEquals(request.pan(), card.getPan());
        assertEquals(request.titular(), card.getTitular());
        assertEquals(request.cedula(), card.getCedula());
        assertEquals(request.tipo().getCodigo(), card.getTipo());
        assertEquals(request.phone(), card.getTelefono());
    }

    @Test
    @DisplayName("Debe mapear correctamente de Card a CardResponse con pan enmascarado")
    void testToResponse() {
        Card card = new Card();
        card.setNumeroValidacion(60);
        card.setPan(new BigDecimal("1234567890123456"));
        card.setIdentificador("c443d8ac50e0add83c571ebec149086f72b39d103dce22c08ce8d1bbaa872be8");

        CardResponse response = cardMapper.toResponse(card);

        assertNotNull(response);
        assertEquals("00", response.status().response());
        assertEquals("Éxito", response.status().message());
        assertEquals(60, response.validationNumer());
        assertEquals("c443d8ac50e0add83c571ebec149086f72b39d103dce22c08ce8d1bbaa872be8", response.identificador());
        assertTrue(response.pan().contains("****"));
    }

    @Test
    @DisplayName("Debe mapear correctamente de Card a CardConsultResponse con pan enmascarado")
    void testToResponseConsult() {
        Card card = new Card();
        card.setPan(new BigDecimal("1234567890123456"));
        card.setTitular("Michael");
        card.setCedula("123456789");
        card.setTelefono(new BigDecimal("1234567895"));
        card.setEstado("ACTIVA");

        CardConsultResponse response = cardMapper.toResponseConsult(card);

        assertNotNull(response);
        assertTrue(response.pan().contains("****"));
        assertEquals("Michael", response.titular());
        assertEquals("123456789", response.cedula());
        assertEquals(new BigDecimal("1234567895"), response.telefono());
        assertEquals("ACTIVA", response.estado());
    }
}
