package com.example.Prueba_Tecnica.IService;

import com.example.Prueba_Tecnica.Dto.DepositDTO;
import com.example.Prueba_Tecnica.Dto.TransferDTO;
import com.example.Prueba_Tecnica.Dto.WithdrawDTO;
import com.example.Prueba_Tecnica.Entity.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransactionService {

    Mono<Void> deposit(DepositDTO depositDTO);

    Mono<Void> withdraw(WithdrawDTO withdrawDTO);

    Mono<Void> transfer(TransferDTO transferDTO);

    Flux<Transaction> getAllTransactions();

    Mono<Transaction> getTransactionById(Long id);
}
