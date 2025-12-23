package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Account;
import com.example.Prueba_Tecnica.Entity.Enums.AccountStatus;
import com.example.Prueba_Tecnica.Entity.Enums.AccountType;
import com.example.Prueba_Tecnica.Repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService accountService;

    private Account savingsAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        savingsAccount = Account.builder()
                .id(1L)
                .accountType(AccountType.SAVINGS)
                .accountNumber("1234567890")
                .status(AccountStatus.ACTIVE)
                .balance(new BigDecimal("0.00"))
                .gmfExempt(false)
                .active(true)
                .clientId(10L)
                .build();
    }

    @Test
    void shouldDeactivateAccount() {
        when(repository.findById(1L)).thenReturn(Mono.just(savingsAccount));
        when(repository.save(any(Account.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(accountService.deactivateAccount(1L))
                .verifyComplete();

        assertEquals(AccountStatus.INACTIVE, savingsAccount.getStatus());
        verify(repository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldCloseAccountWithZeroBalance() {
        when(repository.findById(1L)).thenReturn(Mono.just(savingsAccount));
        when(repository.save(any(Account.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(accountService.cancelAccount(1L))
                .verifyComplete();

        assertEquals(AccountStatus.CLOSED, savingsAccount.getStatus());
        verify(repository, times(1)).save(any(Account.class));
    }
}