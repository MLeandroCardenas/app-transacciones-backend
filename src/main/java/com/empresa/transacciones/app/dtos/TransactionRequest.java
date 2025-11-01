package com.empresa.transacciones.app.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TransactionRequest(
		
	    @NotBlank
	    String identificador,

	    @NotBlank
	    @Size(min = 6, max = 6, message = "El número de referencia debe tener exactamente 6 caracteres")
	    String numeroReferencia,

	    @NotNull
	    BigDecimal totalCompra,

	    String direccion
	) {
	
	
	public TransactionRequest(String identificador, String numeroReferencia, BigDecimal totalCompra) {
        this(identificador, numeroReferencia, totalCompra, null);
    }
	
	public TransactionRequest {
        if (direccion == null || direccion.isBlank()) {
            direccion = "N/A";
        }
    }
}
