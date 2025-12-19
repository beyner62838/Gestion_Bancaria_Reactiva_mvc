package com.example.Prueba_Tecnica.Entity;

import com.example.Prueba_Tecnica.Entity.Enums.AccountStatus;
import com.example.Prueba_Tecnica.Entity.Enums.AccountType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    private Long id;

    @NotNull
    @Column("account_type")
    private AccountType accountType;

    @NotBlank
    @Size(min = 10, max = 10)
    @Column("account_number")
    private String accountNumber;

    @NotNull
    @Column("status")
    private AccountStatus status;

    @NotNull
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Column("balance")
    private BigDecimal balance;

    @Column("gmf_exempt")
    private boolean gmfExempt;

    @Column("creation_date")
    private LocalDateTime creationDate;

    @Column("modification_date")
    private LocalDateTime modificationDate;

    @Column("active")
    private boolean active = true;

    @NotNull
    @Column("client_id")
    private Long clientId;

    public void applyCreateDefaults() {
        LocalDateTime now = LocalDateTime.now();
        if (creationDate == null) creationDate = now;
        modificationDate = now;
        if (balance == null) balance = BigDecimal.ZERO;
        if (status == null) status = AccountStatus.ACTIVE;
        active = true;
    }

    public void touch() {
        modificationDate = LocalDateTime.now();
    }

    public boolean canBeClosed() {
        return balance != null && balance.compareTo(BigDecimal.ZERO) == 0;
    }
}
