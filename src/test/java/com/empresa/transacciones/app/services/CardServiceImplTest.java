package com.empresa.transacciones.app.services;

import com.empresa.transacciones.app.dtos.*;
import com.empresa.transacciones.app.exceptions.BussinesLogicException;
import com.empresa.transacciones.app.exceptions.ModelNotFoundException;
import com.empresa.transacciones.app.mappers.CardMapper;
import com.empresa.transacciones.app.mappers.EnrollMapper;
import com.empresa.transacciones.app.models.Card;
import com.empresa.transacciones.app.repository.CardRepository;
import com.empresa.transacciones.app.utils.enums.CardStatesEnum;
import com.empresa.transacciones.app.utils.enums.CardTypeEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private EnrollMapper enrollMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card card;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        card = new Card();
        card.setIdentificador("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c");
        card.setNumeroValidacion(45);
        card.setEstado(CardStatesEnum.CREADA.name());
    }

    @Test
    @DisplayName("should create card")
    void createCard() {
        CardRequest request = new CardRequest(
        		new BigDecimal("569823698741630156"),
        		"Jose",
        		"25889874589",
        		CardTypeEnum.DEBITO,
        		new BigDecimal("1235898745")
        );
        CardResponse expectedResponse = new CardResponse(
        		new ResponseStatus("00", "Éxito"),
        		3,
        		"569823****0156",
        		"c59e240cead5ea3133d1ba98ec3e92396d82044c42dccf571f3c7cc1e603fe6c"
        );

        when(cardMapper.toEntity(request)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.toResponse(card)).thenReturn(expectedResponse);

        CardResponse response = cardService.createCard(request);

        assertNotNull(response);
        assertEquals("00", response.status().response());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    @DisplayName("should generate card Exepcion")
    void testCreateCard_Exception() {
        CardRequest request = mock(CardRequest.class);
        when(cardMapper.toEntity(request)).thenThrow(new RuntimeException("Error de mapeo"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardService.createCard(request);
        });

        assertTrue(exception.getMessage().contains("Error al crear la tarjeta"));
    }

    @Test
    @DisplayName("should enroll sucesfully the card")
    void testEnrollCard_Success() {
        EnrollRequest request = new EnrollRequest("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c", 45);
        when(enrollMapper.toEntity(request)).thenReturn(card);
        when(cardRepository.findByNumeroValidacion(45)).thenReturn(Optional.of(card));
        when(enrollMapper.toResponse(any(Card.class))).thenReturn(new EnrollResponse(new ResponseStatus("00", "Éxito"), "569823****0156"));

        EnrollResponse response = cardService.enrollCard(request);

        assertNotNull(response);
        assertEquals("00", response.status().response());
        verify(cardRepository).save(card);
        assertEquals(CardStatesEnum.ENROLADA.name(), card.getEstado());
    }

    
    @Test
    @DisplayName("should not enrolled the card")
    void testEnrollCard_NotFound() {
        EnrollRequest request = new EnrollRequest("c09c88f2fcb56c1eed97f0b6621801203d811fca0cbdea4c", 45);
        when(enrollMapper.toEntity(request)).thenReturn(card);
        when(cardRepository.findByNumeroValidacion(999)).thenReturn(Optional.empty());

        assertThrows(ModelNotFoundException.class, () -> cardService.enrollCard(request));
    }
    
    

    @Test
    @DisplayName("should not enrooll for invalid identificator")
    void testEnrollCard_InvalidIdentificador() {
        EnrollRequest request = new EnrollRequest("657e433ff9ec063d749543371e2ea164b51a1a69a88facc85150fff81079ce06", 45);
        when(enrollMapper.toEntity(request)).thenReturn(card);
        when(cardRepository.findByNumeroValidacion(45)).thenReturn(Optional.of(card));

        assertThrows(BussinesLogicException.class, () -> cardService.enrollCard(request));
    }
    
    

    @Test
    @DisplayName("should get ok card")
    void testGetCard_Success() {
        when(cardRepository.findByIdentificador("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c")).thenReturn(Optional.of(card));
        when(cardMapper.toResponseConsult(card)).thenReturn(new CardConsultResponse(
        		"156478****7545",
        		"Michael",
        		"25897412698",
        		new BigDecimal("1234567895"),
        		"ENROLADA"
        ));

        Optional<CardConsultResponse> result = cardService.getCard("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c");

        assertTrue(result.isPresent());
        assertEquals("ENROLADA", result.get().estado());
    }

    
    @Test
    @DisplayName("should not found card")
    void testGetCard_NotFound() {
        when(cardRepository.findByIdentificador("e12e0a773ee6cb46b96b4074c63b1fa")).thenReturn(Optional.empty());
        assertThrows(ModelNotFoundException.class, () -> cardService.getCard("e12e0a773ee6cb46b96b4074c63b1fa"));
    }

    
    @Test
    @DisplayName("should delete card")
    void testDeleteCard_Success() {
        CardDeleteRequest request = new CardDeleteRequest("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c");
        when(cardRepository.findByIdentificador("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c")).thenReturn(Optional.of(card));

        cardService.deleteCard(request);

        verify(cardRepository).save(card);
        assertEquals(CardStatesEnum.INACTIVA.name(), card.getEstado());
    }

    
    @Test
    @DisplayName("should not delete because is inactive")
    void testDeleteCard_AlreadyInactive() {
        card.setEstado(CardStatesEnum.INACTIVA.name());
        CardDeleteRequest request = new CardDeleteRequest("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c");
        when(cardRepository.findByIdentificador("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c")).thenReturn(Optional.of(card));
        assertThrows(BussinesLogicException.class, () -> cardService.deleteCard(request));
    }

    @Test
    @DisplayName("delte card not found")
    void testDeleteCard_NotFound() {
        CardDeleteRequest request = new CardDeleteRequest("999");
        when(cardRepository.findByIdentificador("999")).thenReturn(Optional.empty());
        assertThrows(ModelNotFoundException.class, () -> cardService.deleteCard(request));
    }

}

