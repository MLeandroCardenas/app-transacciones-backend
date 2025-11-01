package com.empresa.transacciones.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.empresa.transacciones.app.models.Card;


@Repository
public interface CardRepository extends CrudRepository<Card, Long> { 
	
	Optional<Card> findByNumeroValidacion(int numeroValidacion);
	Optional<Card> findByIdentificador(String identificador);
}


