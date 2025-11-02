package com.empresa.transacciones.app.services;

import com.empresa.transacciones.app.dtos.ResponseStatus;
import com.empresa.transacciones.app.dtos.TransactionRequest;
import com.empresa.transacciones.app.dtos.TransactionResponse;
import com.empresa.transacciones.app.exceptions.BussinesLogicException;
import com.empresa.transacciones.app.exceptions.ModelNotFoundException;
import com.empresa.transacciones.app.mappers.TransactionMapper;
import com.empresa.transacciones.app.models.Card;
import com.empresa.transacciones.app.models.Transaction;
import com.empresa.transacciones.app.repository.CardRepository;
import com.empresa.transacciones.app.repository.TransactionRepository;
import com.empresa.transacciones.app.utils.commons.Constants;
import com.empresa.transacciones.app.utils.enums.CardStatesEnum;
import com.empresa.transacciones.app.utils.enums.TransactionStatesEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImpTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private TransactionMapper trxMapper;

    @InjectMocks
    private TransactionServiceImp transactionService;

    private TransactionRequest trxRequest;
    private Card card;
    private Transaction trx;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trxRequest = new TransactionRequest("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c", "023458", new BigDecimal("6500000"), "Calle falsa 123");
        card = new Card();
        card.setIdentificador("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c");
        card.setEstado(CardStatesEnum.ENROLADA.name());

        trx = new Transaction();
        trx.setIdentificador("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c");
        trx.setNumeroReferencia("023458");
        trx.setFechaTransaccion(LocalDateTime.now());
        trx.setEstadoTransaccion(TransactionStatesEnum.Aprobada.name());
    }

    @Test
    @DisplayName("create trx wit enrolled card")
    void testCreateTransaction_Success() {
        when(cardRepository.findByIdentificador("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c")).thenReturn(Optional.of(card));
        when(trxMapper.toEntity(trxRequest, TransactionStatesEnum.Aprobada.name(), Constants.TRX_APROVED))
                .thenReturn(trx);
        when(trxMapper.toResponse(trx)).thenReturn(new TransactionResponse(
        		new ResponseStatus("00", "Compra exitosa") , "Aprobada", "023458")
        );

        TransactionResponse response = transactionService.createTransaction(trxRequest);

        assertNotNull(response);
        assertEquals("Aprobada", response.estadoTransaccion());
        verify(transactionRepository).save(trx);
    }

    @Test
    @DisplayName("card not exist")
    void testCreateTransaction_CardNotFound() {
        when(cardRepository.findByIdentificador("c09c88f2fcb9a13f986da")).thenReturn(Optional.empty());
        when(trxMapper.toEntity(trxRequest, TransactionStatesEnum.Rechazada.name(), Constants.CARD_NOT_EXIST))
                .thenReturn(trx);

        assertThrows(ModelNotFoundException.class, () -> transactionService.createTransaction(trxRequest));
        verify(transactionRepository).save(trx);
    }

    @Test
    @DisplayName("card not enrolled")
    void testCreateTransaction_CardNotEnrolled() {
        card.setEstado(CardStatesEnum.CREADA.name());
        when(cardRepository.findByIdentificador("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c")).thenReturn(Optional.of(card));
        when(trxMapper.toEntity(trxRequest, TransactionStatesEnum.Rechazada.name(), Constants.CARD_NOT_ENROLLED))
                .thenReturn(trx);

        assertThrows(BussinesLogicException.class, () -> transactionService.createTransaction(trxRequest));
        verify(transactionRepository).save(trx);
    }

    @Test
    @DisplayName("cancell trx ok")
    void testCancelTransaction_Success() {
        when(transactionRepository.findByIdentificadorAndNumeroReferencia("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c", "023458"))
                .thenReturn(Optional.of(trx));
        when(trxMapper.toResponseCancelTrx(trx))
                .thenReturn(new TransactionResponse(new ResponseStatus("00", Constants.CANCELED_PURCHASE), "Anulada", "023458"));

        TransactionResponse response = transactionService.cancelTransaction(trxRequest);

        assertNotNull(response);
        assertEquals("Anulada", response.estadoTransaccion());
        verify(transactionRepository).save(trx);
        assertEquals(TransactionStatesEnum.Anulada.name(), trx.getEstadoTransaccion());
    }

    @Test
    @DisplayName("cancell trx not found")
    void testCancelTransaction_NotFound() {
        when(transactionRepository.findByIdentificadorAndNumeroReferencia("123", "ABC123"))
                .thenReturn(Optional.empty());

        assertThrows(ModelNotFoundException.class, () -> transactionService.cancelTransaction(trxRequest));
    }

    @Test
    @DisplayName("cancell trx invalid state")
    void testCancelTransaction_InvalidState() {
        trx.setEstadoTransaccion(TransactionStatesEnum.Rechazada.name());
        when(transactionRepository.findByIdentificadorAndNumeroReferencia("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c", "023458"))
                .thenReturn(Optional.of(trx));

        assertThrows(BussinesLogicException.class, () -> transactionService.cancelTransaction(trxRequest));
    }

    @Test
    @DisplayName("cancell trx TooLate")
    void testCancelTransaction_TooLate() {
        trx.setFechaTransaccion(LocalDateTime.now().minusMinutes(6));
        when(transactionRepository.findByIdentificadorAndNumeroReferencia("c09c88f2fcb9a13f986dab8a25756c1eed97f0b6621801203d811fca0cbdea4c", "023458"))
                .thenReturn(Optional.of(trx));

        assertThrows(BussinesLogicException.class, () -> transactionService.cancelTransaction(trxRequest));
    }
    
    @Test
    @DisplayName("getAllTransactions - ok")
    void testGetAllTransactions_Success() {
        Transaction trx1 = new Transaction();
        trx1.setNumeroReferencia("001");
        trx1.setTotalCompra(new BigDecimal("1000"));
        trx1.setEstadoTransaccion("Aprobada");
        trx1.setFechaTransaccion(LocalDateTime.now());
        trx1.setFechaAnulacion(null);

        Transaction trx2 = new Transaction();
        trx2.setNumeroReferencia("002");
        trx2.setTotalCompra(new BigDecimal("2000"));
        trx2.setEstadoTransaccion("Anulada");
        trx2.setFechaTransaccion(LocalDateTime.now().minusMinutes(5));
        trx2.setFechaAnulacion(LocalDateTime.now());

        List<Transaction> trxList = List.of(trx1, trx2);
        when(transactionRepository.findAll()).thenReturn(trxList);

        var result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("001", result.get(0).numeroReferencia());
        assertEquals("Aprobada", result.get(0).estadoTransaccion());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllTransactions - empty list")
    void testGetAllTransactions_EmptyList() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        ModelNotFoundException ex = assertThrows(
            ModelNotFoundException.class,
            () -> transactionService.getAllTransactions()
        );

        assertEquals("01", ex.getCodigo());
        assertEquals("No se encontraron transacciones registradas.", ex.getMessage());
        verify(transactionRepository, times(1)).findAll();
    }

}

