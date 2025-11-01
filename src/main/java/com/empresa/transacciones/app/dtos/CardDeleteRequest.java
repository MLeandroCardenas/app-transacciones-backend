package com.empresa.transacciones.app.dtos;

import jakarta.validation.constraints.NotBlank;

public record CardDeleteRequest(@NotBlank String identificador) {}
