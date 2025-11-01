package com.empresa.transacciones.app.dtos;

public record CardResponse(ResponseStatus status, int validationNumer, String pan, String identificador) { }
