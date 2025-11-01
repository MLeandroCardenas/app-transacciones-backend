package com.empresa.transacciones.app.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CardTypeEnum {

    CREDITO("C"),
    DEBITO("D");

    private final String codigo;

    CardTypeEnum(String codigo) {
        this.codigo = codigo;
    }

    @JsonValue
    public String getCodigo() {
        return codigo;
    }

    @JsonCreator
    public static CardTypeEnum fromCodigo(String codigo) {
        for (CardTypeEnum tipo : CardTypeEnum.values()) {
            // aceptar tanto la letra como el nombre completo
            if (tipo.codigo.equalsIgnoreCase(codigo) || tipo.name().equalsIgnoreCase(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de tarjeta no válido: " + codigo);
    }
}


