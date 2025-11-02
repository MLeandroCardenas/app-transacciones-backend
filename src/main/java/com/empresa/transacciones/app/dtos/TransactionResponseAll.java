package com.empresa.transacciones.app.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseAll(String numeroReferencia, BigDecimal totalCompra, String estadoTransaccion, LocalDateTime fechaTransaccion, LocalDateTime fechaAnulacion) {

}
