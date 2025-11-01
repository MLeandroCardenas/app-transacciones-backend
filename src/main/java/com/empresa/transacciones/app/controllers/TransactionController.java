package com.empresa.transacciones.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empresa.transacciones.app.dtos.ResponseStatus;
import com.empresa.transacciones.app.dtos.TransactionRequest;
import com.empresa.transacciones.app.dtos.TransactionResponse;
import com.empresa.transacciones.app.services.TransactionService;
import com.empresa.transacciones.app.utils.commons.Constants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/transactions")
@Validated
public class TransactionController {
	
	private final TransactionService trxService;
	
	public TransactionController(TransactionService trxService) {
		this.trxService = trxService;
	}
	
	@PostMapping
	public ResponseEntity<TransactionResponse> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
		TransactionResponse result = trxService.createTransaction(transactionRequest);
		TransactionResponse transactionResponse = new TransactionResponse(
				new ResponseStatus("00", Constants.TRX_APROVED),
				result.estadoTransaccion(),
				result.numeroReferencia()
		);
		
		return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionResponse);
	}
	
	
	@PostMapping("/cancel")
	public ResponseEntity<TransactionResponse> cancelTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
		TransactionResponse result = trxService.cancelTransaction(transactionRequest);
		TransactionResponse transactionResponse = new TransactionResponse(new ResponseStatus("00", Constants.CANCELED_PURCHASE), result.numeroReferencia());
		return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionResponse);
	}

	

}
