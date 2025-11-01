package com.empresa.transacciones.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.empresa.transacciones.app.models.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	Optional<Transaction> findByIdentificadorAndNumeroReferencia(String identificador, String numeroReferencia);
}
