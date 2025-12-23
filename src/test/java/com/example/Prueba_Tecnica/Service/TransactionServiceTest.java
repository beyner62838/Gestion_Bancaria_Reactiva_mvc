package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Dto.TransferDTO;
import com.example.Prueba_Tecnica.Entity.Account;
import com.example.Prueba_Tecnica.Entity.Enums.AccountStatus;
import com.example.Prueba_Tecnica.Entity.Enums.AccountType;
import com.example.Prueba_Tecnica.Repository.AccountRepository;
import com.example.Prueba_Tecnica.Repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionalOperator tx;

    @InjectMocks
    private TransactionService transactionService;

    private Account origin;
    private Account dest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // transactional operator passthrough (Mono)
        when(tx.transactional(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // transactional operator passthrough (Flux)
        when(tx.transactional(any(Flux.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        origin = Account.builder()
                .id(1L)
                .accountType(AccountType.SAVINGS)
                .accountNumber("1111111111")
                .status(AccountStatus.ACTIVE)
                .balance(new BigDecimal("800.00"))
                .active(true)
                .clientId(10L)
                .build();

        dest = Account.builder()
                .id(2L)
                .accountType(AccountType.CHECKING)
                .accountNumber("2222222222")
                .status(AccountStatus.ACTIVE)
                .balance(new BigDecimal("700.00"))
                .active(true)
                .clientId(11L)
                .build();
    }
}