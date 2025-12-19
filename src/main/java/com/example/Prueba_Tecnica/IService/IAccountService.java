package com.example.Prueba_Tecnica.IService;

import com.example.Prueba_Tecnica.Entity.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {

    Flux<Account> findAll();

    Mono<Account> findById(Long id);

    Mono<Account> save(Account account);

    Mono<Account> update(Account account, Long id);

    Mono<Void> delete(Long id); // Logical delete

    Mono<Void> activateAccount(Long id);

    Mono<Void> deactivateAccount(Long id);

    Mono<Void> cancelAccount(Long id);
}
