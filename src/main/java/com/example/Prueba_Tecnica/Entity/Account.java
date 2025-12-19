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
import java.util.concurrent.ThreadLocalRandom;

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

    // Relationship is represented as a FK in reactive relational mapping
    @NotNull
    @Column("client_id")
    private Long clientId;

    public void applyCreateDefaults() {
        LocalDateTime now = LocalDateTime.now();
        if (this.creationDate == null) this.creationDate = now;
        this.modificationDate = now;
        if (this.balance == null) this.balance = BigDecimal.ZERO;
        if (this.status == null) this.status = AccountStatus.ACTIVE;
        if (this.accountNumber == null || this.accountNumber.isBlank()) {
            this.accountNumber = generateAccountNumber();
        }
        this.active = true;
    }

    public void touch() {
        this.modificationDate = LocalDateTime.now();
    }

    private static String generateAccountNumber() {
        // 9 digits + 1 Luhn check digit => 10 chars
        int base = ThreadLocalRandom.current().nextInt(100_000_000, 1_000_000_000);
        String nineDigits = String.valueOf(base);
        int check = luhnCheckDigit(nineDigits);
        return nineDigits + check;
    }

    private static int luhnCheckDigit(String numberWithoutCheck) {
        int sum = 0;
        boolean alternate = true; // start doubling from the right-most digit (before check digit)
        for (int i = numberWithoutCheck.length() - 1; i >= 0; i--) {
            int n = numberWithoutCheck.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }

    public boolean canBeClosed() {
        if (this.status == AccountStatus.CLOSED) {
            return false;
        }
        BigDecimal epsilon = new BigDecimal("0.00001");
        boolean zeroBalance = balance != null && balance.abs().compareTo(epsilon) < 0;
        return zeroBalance;
    }
}
