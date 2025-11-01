package com.empresa.transacciones.app.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EnrollRequest(		
		@NotBlank
		String identificador,
		
		@NotNull
		@Positive
		@Max(value = 999, message = "El identificador debe tener máximo 3 dígitos")
		int numeroValidacion
	) { }
