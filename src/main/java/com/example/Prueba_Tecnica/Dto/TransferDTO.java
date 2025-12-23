package com.example.Prueba_Tecnica.Dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDTO {
    @Positive
    private Long originAccountId;

    @Positive
    private Long destinationAccountId;

    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}