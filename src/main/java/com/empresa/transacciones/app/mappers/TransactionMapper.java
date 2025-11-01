package com.empresa.transacciones.app.mappers;

import org.springframework.stereotype.Component;

import com.empresa.transacciones.app.dtos.ResponseStatus;
import com.empresa.transacciones.app.dtos.TransactionRequest;
import com.empresa.transacciones.app.dtos.TransactionResponse;
import com.empresa.transacciones.app.models.Transaction;
import com.empresa.transacciones.app.utils.commons.Constants;
import com.empresa.transacciones.app.utils.enums.TransactionStatesEnum;

@Component
public class TransactionMapper {
	
	public Transaction toEntity(TransactionRequest request, String estado, String motivo) {
		Transaction transaction = new Transaction();
		transaction.setIdentificador(request.identificador());
		transaction.setNumeroReferencia(request.numeroReferencia());
		transaction.setTotalCompra(request.totalCompra());
		transaction.setDireccionCompra(request.direccion());
		transaction.setEstadoTransaccion(estado);
		transaction.setMotivo(motivo);
		
		return transaction;
	}
	
	
	public TransactionResponse toResponse(Transaction trx) {
		ResponseStatus status = new ResponseStatus("00", Constants.TRX_APROVED);
		return new TransactionResponse(status,TransactionStatesEnum.Aprobada.name(),trx.getNumeroReferencia());
	}
	
	public TransactionResponse toResponseCancelTrx(Transaction trx) {
		ResponseStatus status = new ResponseStatus("00", Constants.CANCELED_PURCHASE);
		return new TransactionResponse(status, trx.getNumeroReferencia());
	}

}
