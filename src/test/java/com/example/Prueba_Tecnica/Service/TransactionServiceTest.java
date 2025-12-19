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

        // transactional operator passthrough
        when(tx.transactional(ArgumentMatchers.<Flux<Object>>any()))
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

    @Test
    void shouldTransferMoney() {
        TransferDTO dto = TransferDTO.builder()
                .originAccountId(1L)
                .destinationAccountId(2L)
                .amount(new BigDecimal("200.00"))
                .build();

        when(accountRepository.findById(1L)).thenReturn(Mono.just(origin));
        when(accountRepository.findById(2L)).thenReturn(Mono.just(dest));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(transactionRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(transactionService.transfer(dto))
                .verifyComplete();

        assertEquals(new BigDecimal("600.00"), origin.getBalance());
        assertEquals(new BigDecimal("900.00"), dest.getBalance());
        verify(transactionRepository, times(1)).save(any());
        verify(accountRepository, times(2)).save(any(Account.class));
    }
}