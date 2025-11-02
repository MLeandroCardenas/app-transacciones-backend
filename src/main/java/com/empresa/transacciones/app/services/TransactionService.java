package com.empresa.transacciones.app.services;

import java.util.List;

import com.empresa.transacciones.app.dtos.TransactionRequest;
import com.empresa.transacciones.app.dtos.TransactionResponse;
import com.empresa.transacciones.app.dtos.TransactionResponseAll;

public interface TransactionService {
	
	public TransactionResponse createTransaction(TransactionRequest transactionRequest);
	public TransactionResponse cancelTransaction(TransactionRequest transactionRequest);
	public List<TransactionResponseAll> getAllTransactions();

}
