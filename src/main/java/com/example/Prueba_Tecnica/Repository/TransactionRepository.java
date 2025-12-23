package com.example.Prueba_Tecnica.Repository;

import com.example.Prueba_Tecnica.Entity.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {

    Flux<Transaction> findByOriginAccountIdOrDestinationAccountId(Long originAccountId, Long destinationAccountId);
}
