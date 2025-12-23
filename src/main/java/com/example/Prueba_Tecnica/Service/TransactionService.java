package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Dto.DepositDTO;
import com.example.Prueba_Tecnica.Dto.TransferDTO;
import com.example.Prueba_Tecnica.Dto.WithdrawDTO;
import com.example.Prueba_Tecnica.Entity.Account;
import com.example.Prueba_Tecnica.Entity.Transaction;
import com.example.Prueba_Tecnica.Entity.Enums.TransactionType;
import com.example.Prueba_Tecnica.IService.ITransactionService;
import com.example.Prueba_Tecnica.Repository.AccountRepository;
import com.example.Prueba_Tecnica.Repository.TransactionRepository;
import com.example.Prueba_Tecnica.Util.BusinessException;
import com.example.Prueba_Tecnica.Util.CountMessages;
import com.example.Prueba_Tecnica.Util.TransactionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionalOperator tx;

    @Override
    public Mono<Void> deposit(DepositDTO depositDTO) {
        return accountRepository.findById(depositDTO.getAccountId())
                .filter(Account::isActive)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, CountMessages.COUNT_NOT_FOUND)))
                .flatMap(account -> {
                    account.setBalance(account.getBalance().add(depositDTO.getAmount()));
                    account.touch();

                    Transaction transaction = Transaction.builder()
                            .originAccountId(account.getId())
                            .transactionType(TransactionType.DEPOSIT)
                            .amount(depositDTO.getAmount())
                            .date(LocalDateTime.now())
                            .description("Deposit")
                            .build();

                    return tx.transactional(
                            transactionRepository.save(transaction)
                                    .then(accountRepository.save(account))
                                    .then()
                    );
                });
    }

    @Override
    public Mono<Void> withdraw(WithdrawDTO withdrawDTO) {
        return accountRepository.findById(withdrawDTO.getAccountId())
                .filter(Account::isActive)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, CountMessages.COUNT_NOT_FOUND)))
                .flatMap(account -> {
                    if (account.getBalance().compareTo(withdrawDTO.getAmount()) < 0) {
                        return Mono.error(new BusinessException("Insufficient balance"));
                    }

                    account.setBalance(account.getBalance().subtract(withdrawDTO.getAmount()));
                    account.touch();

                    Transaction transaction = Transaction.builder()
                            .originAccountId(account.getId())
                            .transactionType(TransactionType.WITHDRAWAL)
                            .amount(withdrawDTO.getAmount())
                            .date(LocalDateTime.now())
                            .description("Withdraw")
                            .build();

                    return tx.transactional(
                            transactionRepository.save(transaction)
                                    .then(accountRepository.save(account))
                                    .then()
                    );
                });
    }

    @Override
    public Mono<Void> transfer(TransferDTO transferDTO) {
        Mono<Account> originMono = accountRepository.findById(transferDTO.getOriginAccountId())
                .filter(Account::isActive)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, TransactionMessages.COUNT_ORIGIN_NOT_FOUND)));

        Mono<Account> destMono = accountRepository.findById(transferDTO.getDestinationAccountId())
                .filter(Account::isActive)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, TransactionMessages.COUNT_DESTINATION_NOT_FOUND)));

        return Mono.zip(originMono, destMono)
                .flatMap(tuple -> {
                    Account origin = tuple.getT1();
                    Account destination = tuple.getT2();

                    if (origin.getBalance().compareTo(transferDTO.getAmount()) < 0) {
                        return Mono.error(new BusinessException(TransactionMessages.BALANCE_IS_NEGATIVE));
                    }

                    origin.setBalance(origin.getBalance().subtract(transferDTO.getAmount()));
                    destination.setBalance(destination.getBalance().add(transferDTO.getAmount()));
                    origin.touch();
                    destination.touch();

                    Transaction transaction = Transaction.builder()
                            .originAccountId(origin.getId())
                            .destinationAccountId(destination.getId())
                            .transactionType(TransactionType.TRANSFER)
                            .amount(transferDTO.getAmount())
                            .date(LocalDateTime.now())
                            .description("Transfer between accounts")
                            .build();

                    return tx.transactional(
                            transactionRepository.save(transaction)
                                    .then(accountRepository.save(origin))
                                    .then(accountRepository.save(destination))
                                    .then()
                    );
                });
    }

    @Override
    public Flux<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Mono<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, TransactionMessages.TRANSACTION_NOT_FOUND)));
    }
}
