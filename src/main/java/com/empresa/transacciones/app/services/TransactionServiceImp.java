package com.empresa.transacciones.app.services;
import com.empresa.transacciones.app.repository.TransactionRepository;
import com.empresa.transacciones.app.utils.commons.Constants;
import com.empresa.transacciones.app.utils.enums.CardStatesEnum;
import com.empresa.transacciones.app.utils.enums.TransactionStatesEnum;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.empresa.transacciones.app.dtos.TransactionRequest;
import com.empresa.transacciones.app.dtos.TransactionResponse;
import com.empresa.transacciones.app.exceptions.BussinesLogicException;
import com.empresa.transacciones.app.exceptions.ModelNotFoundException;
import com.empresa.transacciones.app.mappers.TransactionMapper;
import com.empresa.transacciones.app.models.Card;
import com.empresa.transacciones.app.models.Transaction;
import com.empresa.transacciones.app.repository.CardRepository;

@Service
public class TransactionServiceImp implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final TransactionMapper trxMapper;

    TransactionServiceImp(
    		TransactionRepository transactionRepository, 
    		CardRepository cardRepository,
    		TransactionMapper trxMapper
    		) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.trxMapper = trxMapper;
    }

	@Override
	public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
		Optional<Card> card = cardRepository.findByIdentificador(transactionRequest.identificador());
		
		if(!card.isPresent()) {
			Transaction trx = trxMapper.toEntity(transactionRequest, TransactionStatesEnum.Rechazada.name(), Constants.CARD_NOT_EXIST);
			transactionRepository.save(trx);
			throw new ModelNotFoundException("01", Constants.CARD_NOT_EXIST);
		}
		
		if(!CardStatesEnum.ENROLADA.name().equals(card.get().getEstado())) {
			Transaction trx = trxMapper.toEntity(transactionRequest, TransactionStatesEnum.Rechazada.name(), Constants.CARD_NOT_ENROLLED);
			transactionRepository.save(trx);
			throw new BussinesLogicException("02", Constants.CARD_NOT_ENROLLED);
			
		}
		
		Transaction trx = trxMapper.toEntity(transactionRequest, TransactionStatesEnum.Aprobada.name(), Constants.TRX_APROVED);
		transactionRepository.save(trx);
		return trxMapper.toResponse(trx);
	}

	@Override
	public TransactionResponse cancelTransaction(TransactionRequest transactionRequest) {
		Transaction trx = transactionRepository.findByIdentificadorAndNumeroReferencia(transactionRequest.identificador(), transactionRequest.numeroReferencia())
	            .orElseThrow(() -> new ModelNotFoundException("01", Constants.INVALID_REFERENCE_NUMBER));
		
		if(!TransactionStatesEnum.Aprobada.name().equals(trx.getEstadoTransaccion())) {
			throw new BussinesLogicException("03", Constants.TRX_STATE_INVALID);
		}
		
		this.validateTransactionTime(trx.getFechaTransaccion());
		
		trx.setEstadoTransaccion(TransactionStatesEnum.Anulada.name());
		trx.setFechaAnulacion(LocalDateTime.now());
		trx.setMotivo(Constants.TRX_CAUSE_CANCELATION);
		transactionRepository.save(trx);
		
		return trxMapper.toResponseCancelTrx(trx);
	}
	
	/**
	 * Valida que no hayan pasado mas de 5 minutos en la transaccion para poder anularla
	 * @param trxDate
	 */
	private void validateTransactionTime(LocalDateTime trxDate) {
		long minutes = ChronoUnit.MINUTES.between(trxDate, LocalDateTime.now());
		if(minutes > 5) {
			throw new BussinesLogicException("02", Constants.TRX_NEGATIVE);
		}
		
	}

}
