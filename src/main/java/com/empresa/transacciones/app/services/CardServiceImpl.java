package com.empresa.transacciones.app.services;

import com.empresa.transacciones.app.dtos.CardConsultResponse;
import com.empresa.transacciones.app.dtos.CardDeleteRequest;
import com.empresa.transacciones.app.dtos.CardRequest;
import com.empresa.transacciones.app.dtos.CardResponse;
import com.empresa.transacciones.app.dtos.EnrollRequest;
import com.empresa.transacciones.app.dtos.EnrollResponse;
import com.empresa.transacciones.app.exceptions.BussinesLogicException;
import com.empresa.transacciones.app.exceptions.ModelNotFoundException;
import com.empresa.transacciones.app.mappers.CardMapper;
import com.empresa.transacciones.app.mappers.EnrollMapper;
import com.empresa.transacciones.app.models.Card;
import com.empresa.transacciones.app.repository.CardRepository;
import com.empresa.transacciones.app.utils.enums.CardStatesEnum;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final EnrollMapper enrollMapper;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper, EnrollMapper enrollMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.enrollMapper = enrollMapper;
    }


    @Override
    public CardResponse createCard(CardRequest cardRequest) {
        try {
            Card card = cardMapper.toEntity(cardRequest);
            Card savedCard = cardRepository.save(card);
            return cardMapper.toResponse(savedCard);
        } catch (Exception ex) {
            throw new RuntimeException("Error al crear la tarjeta: " + ex.getMessage(), ex);
        }
    }


	@Override
	public EnrollResponse enrollCard(EnrollRequest enrollRequest) {
		Card card = enrollMapper.toEntity(enrollRequest);
		Card foundCard = this.cardRepository
	            .findByNumeroValidacion(card.getNumeroValidacion())
	            .orElseThrow(() -> new ModelNotFoundException("01", "Tarjeta no existe"));
		
		if(!enrollRequest.identificador().equals(foundCard.getIdentificador())) {
			throw new BussinesLogicException("02", "Número de validación inválido");
		}
		
		foundCard.setEstado(CardStatesEnum.ENROLADA.name());
		this.cardRepository.save(foundCard);
		return enrollMapper.toResponse(foundCard);
	}


	@Override
	public Optional<CardConsultResponse> getCard(String identificador) {
		 Card card = cardRepository.findByIdentificador(identificador)
		            .orElseThrow(() -> new ModelNotFoundException("01", "Tarjeta no existe"));
		return Optional.of(cardMapper.toResponseConsult(card));
	}


	@Override
	public void deleteCard(CardDeleteRequest cardDeleteRequest) {
		Card cardFound = cardRepository.findByIdentificador(cardDeleteRequest.identificador())
	            .orElseThrow(() -> new ModelNotFoundException("01", "No se ha eliminado la tarjeta"));
		
		if(CardStatesEnum.INACTIVA.name().equals(cardFound.getEstado())) {
			throw new BussinesLogicException("01", "La tarjeta ya se encuentra inactiva");
		}
		
		cardFound.setEstado(CardStatesEnum.INACTIVA.name());
		cardRepository.save(cardFound);
	}

}
