package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Account;
import com.example.Prueba_Tecnica.Entity.Enums.AccountStatus;
import com.example.Prueba_Tecnica.IService.IAccountService;
import com.example.Prueba_Tecnica.Repository.AccountRepository;
import com.example.Prueba_Tecnica.Util.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.Prueba_Tecnica.Util.CountMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository repository;

    @Override
    public Flux<Account> findAll() {
        return repository.findAllByActiveTrue();
    }

    @Override
    public Mono<Account> findById(Long id) {
        return repository.findById(id)
                .filter(Account::isActive)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, CountMessages.COUNT_NOT_FOUND + id)));
    }

    @Override
    public Mono<Account> save(Account account) {
        account.applyCreateDefaults();
        // id must be null for insert (DB generates it)
        account.setId(null);
        return repository.save(account);
    }

    @Override
    public Mono<Account> update(Account incoming, Long id) {
        return findById(id)
                .flatMap(existing -> {
                    existing.setAccountType(incoming.getAccountType());
                    existing.setGmfExempt(incoming.isGmfExempt());
                    // status: keep existing unless explicitly provided
                    if (incoming.getStatus() != null) {
                        existing.setStatus(incoming.getStatus());
                    }
                    if (incoming.getBalance() != null) {
                        if (incoming.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                            return Mono.error(new BusinessException("Balance cannot be negative"));
                        }
                        existing.setBalance(incoming.getBalance());
                    }
                    existing.touch();
                    return repository.save(existing);
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return findById(id)
                .flatMap(acc -> {
                    acc.setActive(false);
                    acc.touch();
                    return repository.save(acc).then();
                });
    }

    @Override
    public Mono<Void> activateAccount(Long id) {
        return findById(id)
                .flatMap(acc -> {
                    acc.setStatus(AccountStatus.ACTIVE);
                    acc.touch();
                    return repository.save(acc).then();
                });
    }

    @Override
    public Mono<Void> deactivateAccount(Long id) {
        return findById(id)
                .flatMap(acc -> {
                    acc.setStatus(AccountStatus.INACTIVE);
                    acc.touch();
                    return repository.save(acc).then();
                });
    }

    @Override
    public Mono<Void> cancelAccount(Long id) {
        return findById(id)
                .flatMap(acc -> {
                    if (acc.getBalance() != null && acc.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                        acc.setStatus(AccountStatus.CLOSED);
                        acc.touch();
                        return repository.save(acc).then();
                    }
                    return Mono.error(new BusinessException(CountMessages.NOT_COUNT_CANCELLED));
                });
    }
}
