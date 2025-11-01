package com.empresa.transacciones.app.dtos;

import java.math.BigDecimal;

public record CardConsultResponse(String pan, String titular, String cedula, BigDecimal telefono, String estado) {}
