package com.empresa.transacciones.app.dtos;

import com.empresa.transacciones.app.utils.enums.CardTypeEnum;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CardRequest(
        @NotNull
        @DecimalMin("1000000000000000")
        @DecimalMax("9999999999999999999")
        BigDecimal pan,

        @NotBlank
        @Size(max = 100)
        String titular,

        @NotBlank
        @Size(min = 10, max = 15)
        String cedula,

        @NotNull
        CardTypeEnum tipo,

        @NotNull
        @DecimalMin("1000000000") // phone 10 dígitos, sigue en Integer
        @DecimalMax("9999999999")
        BigDecimal phone
) { }
