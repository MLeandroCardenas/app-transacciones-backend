package com.empresa.transacciones.app.exceptions;

public class BussinesLogicException extends RuntimeException {

	private static final long serialVersionUID = -8078435866611308239L;
	
	 private final String codigo;
	
	 public BussinesLogicException(String codigo, String mensaje) {
	        super(mensaje);
	        this.codigo = codigo;
	    }

	    public String getCodigo() {
	        return codigo;
	    }

}
