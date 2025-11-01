package com.empresa.transacciones.app.dtos;

public record TransactionResponse(ResponseStatus resultado, String estadoTransaccion, String numeroReferencia) {
	
	public TransactionResponse(ResponseStatus resultado, String numeroReferencia) {
		this(resultado, null, numeroReferencia);
	}
}
