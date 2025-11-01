package com.empresa.transacciones.app.exceptions;

public class ModelNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1398702460092456964L;

    private final String codigo; 

    public ModelNotFoundException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
