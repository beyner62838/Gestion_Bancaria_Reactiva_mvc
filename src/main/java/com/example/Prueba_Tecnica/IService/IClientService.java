package com.example.Prueba_Tecnica.IService;

import com.example.Prueba_Tecnica.Entity.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClientService {

    Mono<Client> findByIdentificationNumber(String identificationNumber);

    Mono<Boolean> existsByIdentificationNumber(String identificationNumber);

    Mono<Boolean> existsByEmail(String email);

    Flux<Client> findAll();

    Mono<Client> findById(Long id);

    Mono<Client> save(Client client);

    Mono<Client> update(Client client, Long id);

    Mono<Void> delete(Long id); // Logical delete
}
