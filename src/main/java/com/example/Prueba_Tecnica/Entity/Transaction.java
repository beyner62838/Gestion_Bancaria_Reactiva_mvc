package com.example.Prueba_Tecnica.Entity;

import com.example.Prueba_Tecnica.Entity.Enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    private Long id;

    @NotNull
    @Column("transaction_type")
    private TransactionType transactionType;

    @NotNull
    @Positive(message = "The amount must be positive")
    @Column("amount")
    private BigDecimal amount;

    @NotNull
    @Column("date")
    private LocalDateTime date;

    @NotNull
    @Column("origin_account_id")
    private Long originAccountId;

    @Column("destination_account_id")
    private Long destinationAccountId;

    @Column("description")
    private String description;
}
