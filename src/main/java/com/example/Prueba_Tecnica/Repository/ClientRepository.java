package com.example.Prueba_Tecnica.Repository;

import com.example.Prueba_Tecnica.Entity.Client;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveCrudRepository<Client, Long> {

    Mono<Client> findByIdentificationNumber(String identificationNumber);

    Mono<Boolean> existsByIdentificationNumber(String identificationNumber);

    Mono<Boolean> existsByEmail(String email);

    Flux<Client> findAllByActiveTrue();
}
