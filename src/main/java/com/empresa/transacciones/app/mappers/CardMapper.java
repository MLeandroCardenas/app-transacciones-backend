package com.empresa.transacciones.app.mappers;

import com.empresa.transacciones.app.dtos.CardConsultResponse;
import com.empresa.transacciones.app.dtos.CardRequest;
import com.empresa.transacciones.app.dtos.CardResponse;
import com.empresa.transacciones.app.dtos.ResponseStatus;
import com.empresa.transacciones.app.models.Card;
import com.empresa.transacciones.app.utils.commons.HashUtil;

import org.springframework.stereotype.Component;


@Component
public class CardMapper {

    public Card toEntity(CardRequest request) {
        Card card = new Card();
        card.setPan(request.pan());
        card.setTitular(request.titular());
        card.setCedula(request.cedula());
        card.setTipo(request.tipo().getCodigo());
        card.setTelefono(request.phone());
        return card;
    }

    public CardResponse toResponse(Card card) {
    	ResponseStatus status = new ResponseStatus("00", "Éxito");
        return new CardResponse(
        		status,
                card.getNumeroValidacion(),
                HashUtil.maskPan(card.getPan().toPlainString()),
                card.getIdentificador()
        );
    }
    
    
    public CardConsultResponse toResponseConsult(Card card) {
    	return new CardConsultResponse(
    			HashUtil.maskPan(card.getPan().toPlainString()),
    			card.getTitular(),
    			card.getCedula(),
    			card.getTelefono(),
    			card.getEstado()
    	);
    }

}

