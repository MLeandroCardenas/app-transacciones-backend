package com.empresa.transacciones.app.services;

import com.empresa.transacciones.app.dtos.TransactionRequest;
import com.empresa.transacciones.app.dtos.TransactionResponse;

public interface TransactionService {
	
	public TransactionResponse createTransaction(TransactionRequest transactionRequest);
	public TransactionResponse cancelTransaction(TransactionRequest transactionRequest);

}
