package com.empresa.transacciones.app.controllers;


import com.empresa.transacciones.app.dtos.CardConsultResponse;
import com.empresa.transacciones.app.dtos.CardDeleteRequest;
import com.empresa.transacciones.app.dtos.CardRequest;
import com.empresa.transacciones.app.dtos.CardResponse;
import com.empresa.transacciones.app.dtos.EnrollRequest;
import com.empresa.transacciones.app.dtos.EnrollResponse;
import com.empresa.transacciones.app.dtos.ResponseStatus;
import com.empresa.transacciones.app.services.CardService;
import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cards")
@Validated
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    @PostMapping
    public ResponseEntity<CardResponse> createCard(@RequestBody @Valid CardRequest cardRequest) {
        CardResponse response = cardService.createCard(cardRequest);
        ResponseStatus status = new ResponseStatus("00", "Éxito");
        CardResponse cardResponse = new CardResponse(
                status,
                response.validationNumer(),
                response.pan(),
                response.identificador()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cardResponse);
    }
    
    
    @PostMapping("/enrolar")
    public ResponseEntity<EnrollResponse> cardEnroll(@RequestBody @Valid EnrollRequest enrollRequest) {
    	EnrollResponse response = cardService.enrollCard(enrollRequest);
        ResponseStatus status = new ResponseStatus("00", "Éxito");
        EnrollResponse enrollResponse = new EnrollResponse(
                status,
                response.pan()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(enrollResponse);
    }
    
    
	@GetMapping("/card")
	public ResponseEntity<CardConsultResponse> getCard(@RequestParam String identificador) {
		
		Optional<CardConsultResponse> cardResponse = cardService.getCard(identificador);
		CardConsultResponse response = null;
		if(cardResponse.isPresent()) {			
			response = new CardConsultResponse(
					cardResponse.get().pan(),
					cardResponse.get().titular(),
					cardResponse.get().cedula(),
					cardResponse.get().telefono(),
					cardResponse.get().estado()
				);
		}
		return ResponseEntity.ok(response);
	}
	
	
	@PutMapping("/delete")
	public ResponseEntity<ResponseStatus> deleteCard(@RequestBody @Valid CardDeleteRequest cardDeleteRequest) {
		cardService.deleteCard(cardDeleteRequest);
		return  ResponseEntity.ok(new ResponseStatus("00", "Se ha eliminado la tarjeta"));
	}
    
    
    
    
    
    
}
