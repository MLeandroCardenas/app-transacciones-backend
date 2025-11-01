package com.empresa.transacciones.app.filters;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.empresa.transacciones.app.dtos.ResponseStatus;
import com.empresa.transacciones.app.exceptions.BussinesLogicException;
import com.empresa.transacciones.app.exceptions.ModelNotFoundException;

import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;


import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class FilterExceptions extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(BussinesLogicException.class)
	public final ResponseEntity<ResponseStatus> bussinesException(BussinesLogicException ex) {
		ResponseStatus response = new ResponseStatus(ex.getCodigo(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
	}
	
	@ExceptionHandler(ModelNotFoundException.class)
	public final ResponseEntity<ResponseStatus> modelNotFoundException(ModelNotFoundException ex) {
		ResponseStatus response = new ResponseStatus(ex.getCodigo(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(response);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<ResponseStatus> dataIntegrityViolationException(DataIntegrityViolationException ex) {
		ResponseStatus response = new ResponseStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<ResponseStatus> constraintViolationException(ConstraintViolationException ex) {
		ResponseStatus response = new ResponseStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
	}
	
	@ExceptionHandler(SQLException.class)
	public final ResponseEntity<ResponseStatus> constraintViolationException(SQLException ex) {
		ResponseStatus response = new ResponseStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(response);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public final ResponseEntity<ResponseStatus> nullPointerException(NullPointerException ex) {
		ResponseStatus response = new ResponseStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(response);
	}
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ResponseStatus> exception(Exception ex) {
		ResponseStatus response = new ResponseStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(response);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
	        MethodArgumentNotValidException ex,
	        HttpHeaders headers,
	        org.springframework.http.HttpStatusCode status,
	        WebRequest request) {

	    BindingResult resultado = ex.getBindingResult();
	    List<FieldError> fieldErrors = resultado.getFieldErrors();

	    StringBuilder errorMessage = new StringBuilder("Errores de validación: ");
	    fieldErrors.forEach(f -> errorMessage.append(f.getField())
	            .append(" -> ")
	            .append(f.getDefaultMessage())
	            .append("; "));

	    ResponseStatus response = new ResponseStatus(
	            String.valueOf(HttpStatus.BAD_REQUEST.value()),
	            errorMessage.toString()
	    );

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
	        HttpMessageNotReadableException ex,
	        HttpHeaders headers,
	        org.springframework.http.HttpStatusCode status,
	        WebRequest request) {

	    String mensaje = "El cuerpo JSON está mal formado.";
	    ResponseStatus response = new ResponseStatus(
	            String.valueOf(HttpStatus.BAD_REQUEST.value()),
	            mensaje
	    );

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
	        HttpRequestMethodNotSupportedException ex,
	        HttpHeaders headers,
	        org.springframework.http.HttpStatusCode status,
	        WebRequest request) {

	    ResponseStatus response = new ResponseStatus(
	            String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()),
	            "Método HTTP no soportado. Usa: " + ex.getSupportedHttpMethods()
	    );

	    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
	        HttpMediaTypeNotSupportedException ex,
	        HttpHeaders headers,
	        org.springframework.http.HttpStatusCode status,
	        WebRequest request) {

	    ResponseStatus response = new ResponseStatus(
	            String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()),
	            "Formato de contenido no soportado (Content-Type inválido)."
	    );

	    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(
	        TypeMismatchException ex,
	        HttpHeaders headers,
	        org.springframework.http.HttpStatusCode status,
	        WebRequest request) {

	    ResponseStatus response = new ResponseStatus(
	            String.valueOf(HttpStatus.BAD_REQUEST.value()),
	            "Tipo de parámetro incorrecto: " + ex.getPropertyName()
	    );

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
	        NoHandlerFoundException ex,
	        HttpHeaders headers,
	        org.springframework.http.HttpStatusCode status,
	        WebRequest request) {

	    ResponseStatus response = new ResponseStatus(
	            String.valueOf(HttpStatus.NOT_FOUND.value()),
	            "La URL solicitada no existe o no fue encontrada."
	    );

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
	        HttpMediaTypeNotAcceptableException ex,
	        HttpHeaders headers,
	        org.springframework.http.HttpStatusCode status,
	        WebRequest request) {

	    ResponseStatus response = new ResponseStatus(
	            String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()),
	            "Encabezados HTTP no aceptables o formato de respuesta no soportado."
	    );

	    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
	}



	

}
