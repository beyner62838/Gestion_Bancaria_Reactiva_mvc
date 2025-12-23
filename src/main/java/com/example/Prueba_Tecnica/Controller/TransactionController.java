package com.example.Prueba_Tecnica.Controller;

import com.example.Prueba_Tecnica.Dto.DepositDTO;
import com.example.Prueba_Tecnica.Dto.TransferDTO;
import com.example.Prueba_Tecnica.Dto.WithdrawDTO;
import com.example.Prueba_Tecnica.Entity.Transaction;
import com.example.Prueba_Tecnica.IService.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping("/deposit")
    public Mono<ResponseEntity<String>> deposit(@RequestBody DepositDTO depositDTO) {
        return transactionService.deposit(depositDTO)
                .thenReturn(ResponseEntity.ok("Deposit completed successfully"));
    }

    @PostMapping("/withdraw")
    public Mono<ResponseEntity<String>> withdraw(@RequestBody WithdrawDTO withdrawDTO) {
        return transactionService.withdraw(withdrawDTO)
                .thenReturn(ResponseEntity.ok("Withdrawal completed successfully"));
    }

    @PostMapping("/transfer")
    public Mono<ResponseEntity<String>> transfer(@RequestBody TransferDTO transferDTO) {
        return transactionService.transfer(transferDTO)
                .thenReturn(ResponseEntity.ok("Transfer completed successfully"));
    }

    @GetMapping
    public Flux<Transaction> list() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Mono<Transaction> getById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }
}
