package com.empresa.transacciones.app.filters;

import com.empresa.transacciones.app.dtos.ResponseStatus;
import com.empresa.transacciones.app.exceptions.BussinesLogicException;
import com.empresa.transacciones.app.exceptions.ModelNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.*;
import org.springframework.web.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilterExceptionsTest {

    private FilterExceptions filterExceptions;

    @BeforeEach
    void setUp() {
        filterExceptions = new FilterExceptions();
    }

    @Test
    @DisplayName("Debe manejar BussinesLogicException con código 400")
    void testBussinesLogicException() {
        BussinesLogicException ex = new BussinesLogicException("99", "Error de negocio");
        ResponseEntity<ResponseStatus> response = filterExceptions.bussinesException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("99", response.getBody().response());
    }

    @Test
    @DisplayName("Debe manejar ModelNotFoundException con código 404")
    void testModelNotFoundException() {
        ModelNotFoundException ex = new ModelNotFoundException("01", "No encontrado");
        ResponseEntity<ResponseStatus> response = filterExceptions.modelNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("01", response.getBody().response());
    }

    @Test
    @DisplayName("Debe manejar DataIntegrityViolationException con código 400")
    void testDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Violación de integridad");
        ResponseEntity<ResponseStatus> response = filterExceptions.dataIntegrityViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().message().contains("Violación"));
    }

    @Test
    @DisplayName("Debe manejar ConstraintViolationException con código 400")
    void testConstraintViolationException() {
        ConstraintViolationException ex = new ConstraintViolationException("Error de constraint", Set.of());
        ResponseEntity<ResponseStatus> response = filterExceptions.constraintViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe manejar SQLException con código 500")
    void testSQLException() {
        SQLException ex = new SQLException("Falla SQL");
        ResponseEntity<ResponseStatus> response = filterExceptions.constraintViolationException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().message().contains("Falla SQL"));
    }

    @Test
    @DisplayName("Debe manejar NullPointerException con código 500")
    void testNullPointerException() {
        NullPointerException ex = new NullPointerException("Nulo encontrado");
        ResponseEntity<ResponseStatus> response = filterExceptions.nullPointerException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe manejar Exception genérica con código 500")
    void testException() {
        Exception ex = new Exception("Error general");
        ResponseEntity<ResponseStatus> response = filterExceptions.exception(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe manejar MethodArgumentNotValidException con detalles de campo")
    void testHandleMethodArgumentNotValid() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("obj", "campo", "requerido");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Object> response = filterExceptions.handleMethodArgumentNotValid(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, mock(WebRequest.class));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("campo"));
    }

    @Test
    @DisplayName("Debe manejar HttpMessageNotReadableException")
    void testHandleHttpMessageNotReadable() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("JSON mal formado");
        ResponseEntity<Object> response = filterExceptions.handleHttpMessageNotReadable(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, mock(WebRequest.class));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("JSON"));
    }


    @Test
    @DisplayName("Debe manejar HttpMediaTypeNotSupportedException")
    void testHandleHttpMediaTypeNotSupported() {
        HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException("tipo no soportado");
        ResponseEntity<Object> response = filterExceptions.handleHttpMediaTypeNotSupported(
                ex, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, mock(WebRequest.class));

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe manejar TypeMismatchException")
    void testHandleTypeMismatch() {
        TypeMismatchException ex = new TypeMismatchException("abc", Integer.class);
        ex.initPropertyName("id");
        ResponseEntity<Object> response = filterExceptions.handleTypeMismatch(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, mock(WebRequest.class));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("id"));
    }

    @Test
    @DisplayName("Debe manejar NoHandlerFoundException")
    void testHandleNoHandlerFoundException() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/no/existe", new HttpHeaders());
        ResponseEntity<Object> response = filterExceptions.handleNoHandlerFoundException(
                ex, new HttpHeaders(), HttpStatus.NOT_FOUND, mock(WebRequest.class));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe manejar HttpMediaTypeNotAcceptableException")
    void testHandleHttpMediaTypeNotAcceptable() {
        HttpMediaTypeNotAcceptableException ex = new HttpMediaTypeNotAcceptableException("No aceptable");
        ResponseEntity<Object> response = filterExceptions.handleHttpMediaTypeNotAcceptable(
                ex, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, mock(WebRequest.class));

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }
   
    
}
