package com.example.Prueba_Tecnica.Dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositDTO {
    @Positive
    private Long accountId;

    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}