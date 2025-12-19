package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Account;
import com.example.Prueba_Tecnica.Entity.Enums.AccountStatus;
import com.example.Prueba_Tecnica.Entity.Enums.AccountType;
import com.example.Prueba_Tecnica.IService.IAccountService;
import com.example.Prueba_Tecnica.Repository.AccountRepository;
import com.example.Prueba_Tecnica.Util.BusinessException;
import com.example.Prueba_Tecnica.Util.CountMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

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
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                CountMessages.COUNT_NOT_FOUND + id
                        )
                ));
    }


    @Override
    public Mono<Account> save(Account account) {
        account.setId(null);
        account.applyCreateDefaults();

        return generateUniqueAccountNumber(account.getAccountType())
                .map(number -> {
                    account.setAccountNumber(number);
                    account.setCreationDate(LocalDateTime.now());
                    account.setModificationDate(LocalDateTime.now());
                    return account;
                })
                .flatMap(repository::save);
    }

    @Override
    public Mono<Account> update(Account incoming, Long id) {
        return findById(id)
                .flatMap(existing -> {

                    existing.setAccountType(incoming.getAccountType());
                    existing.setGmfExempt(incoming.isGmfExempt());

                    if (incoming.getStatus() != null) {
                        existing.setStatus(incoming.getStatus());
                    }

                    if (incoming.getBalance() != null) {
                        if (incoming.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                            return Mono.error(
                                    new BusinessException("Balance cannot be negative")
                            );
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
        return changeStatus(id, AccountStatus.ACTIVE);
    }

    @Override
    public Mono<Void> deactivateAccount(Long id) {
        return changeStatus(id, AccountStatus.INACTIVE);
    }

    @Override
    public Mono<Void> cancelAccount(Long id) {
        return findById(id)
                .flatMap(acc -> {
                    if (!acc.canBeClosed()) {
                        return Mono.error(
                                new BusinessException(CountMessages.NOT_COUNT_CANCELLED)
                        );
                    }
                    acc.setStatus(AccountStatus.CLOSED);
                    acc.touch();
                    return repository.save(acc).then();
                });
    }

    private Mono<Void> changeStatus(Long id, AccountStatus status) {
        return findById(id)
                .flatMap(acc -> {
                    acc.setStatus(status);
                    acc.touch();
                    return repository.save(acc).then();
                });
    }

    // ========================
    // ACCOUNT NUMBER LOGIC
    // ========================

    private Mono<String> generateUniqueAccountNumber(AccountType type) {
        return Mono.defer(() -> {
            String prefix = getPrefixByType(type);
            String candidate = prefix + randomDigits(8);

            return repository.existsByAccountNumber(candidate)
                    .flatMap(exists -> exists
                            ? generateUniqueAccountNumber(type)
                            : Mono.just(candidate)
                    );
        });
    }

    private String getPrefixByType(AccountType type) {
        if (type == AccountType.SAVINGS) {
            return "53";
        }
        if (type == AccountType.CHECKING) {
            return "33";
        }
        throw new BusinessException("Invalid account type");
    }

    private String randomDigits(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;
        return String.valueOf(
                ThreadLocalRandom.current().nextInt(min, max)
        );
    }
}
