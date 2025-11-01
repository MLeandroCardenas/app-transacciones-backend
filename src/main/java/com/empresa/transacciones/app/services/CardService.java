package com.empresa.transacciones.app.services;

import java.util.Optional;

import com.empresa.transacciones.app.dtos.CardConsultResponse;
import com.empresa.transacciones.app.dtos.CardDeleteRequest;
import com.empresa.transacciones.app.dtos.CardRequest;
import com.empresa.transacciones.app.dtos.CardResponse;
import com.empresa.transacciones.app.dtos.EnrollRequest;
import com.empresa.transacciones.app.dtos.EnrollResponse;



public interface CardService {
    public CardResponse createCard(CardRequest cardRequest);
    public EnrollResponse enrollCard(EnrollRequest enrollRequest);
    public Optional<CardConsultResponse> getCard(String identificador);
    public void deleteCard(CardDeleteRequest cardDeleteRequest);
}
