package com.empresa.transacciones.app.mappers;

import org.springframework.stereotype.Component;

import com.empresa.transacciones.app.dtos.EnrollRequest;
import com.empresa.transacciones.app.dtos.EnrollResponse;
import com.empresa.transacciones.app.dtos.ResponseStatus;
import com.empresa.transacciones.app.models.Card;
import com.empresa.transacciones.app.utils.commons.HashUtil;

@Component
public class EnrollMapper {
	
	public Card toEntity(EnrollRequest request) {
        Card card = new Card();
        card.setIdentificador(request.identificador());
        card.setNumeroValidacion(request.numeroValidacion());
        return card;
    }
	
	public EnrollResponse toResponse(Card card) {
		ResponseStatus status = new ResponseStatus("00", "Éxito");
		return new EnrollResponse(status, HashUtil.maskPan(card.getPan().toPlainString()));
	}

}
