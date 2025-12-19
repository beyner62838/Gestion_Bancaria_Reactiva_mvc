package com.example.Prueba_Tecnica.Repository;

import com.example.Prueba_Tecnica.Entity.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {
    Mono<Boolean> existsByAccountNumber(String accountNumber);

    Flux<Account> findAllByActiveTrue();

    Mono<Account> findByAccountNumber(String accountNumber);

    Flux<Account> findByClientIdAndActiveTrue(Long clientId);
}
