package com.example.Prueba_Tecnica.Controller;

import com.example.Prueba_Tecnica.Entity.Account;
import com.example.Prueba_Tecnica.IService.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final IAccountService accountService;

    @GetMapping
    public Flux<Account> findAll() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Account> findById(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @PostMapping
    public Mono<Account> create(@Valid @RequestBody Account account) {
        return accountService.save(account);
    }

    @PutMapping("/{id}")
    public Mono<Account> update(@Valid @RequestBody Account account, @PathVariable Long id) {
        return accountService.update(account, id);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return accountService.delete(id).thenReturn(ResponseEntity.noContent().build());
    }

    @PutMapping("/activate/{id}")
    public Mono<ResponseEntity<String>> activateAccount(@PathVariable Long id) {
        return accountService.activateAccount(id)
                .thenReturn(ResponseEntity.ok("Account activated successfully"));
    }

    @PutMapping("/deactivate/{id}")
    public Mono<ResponseEntity<String>> deactivateAccount(@PathVariable Long id) {
        return accountService.deactivateAccount(id)
                .thenReturn(ResponseEntity.ok("Account deactivated successfully"));
    }

    @PutMapping("/cancel/{id}")
    public Mono<ResponseEntity<String>> cancelAccount(@PathVariable Long id) {
        return accountService.cancelAccount(id)
                .thenReturn(ResponseEntity.ok("Account cancelled successfully"));
    }
}
