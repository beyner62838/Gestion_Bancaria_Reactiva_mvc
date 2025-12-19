package com.example.Prueba_Tecnica.Dto;

import com.example.Prueba_Tecnica.Entity.Enums.IdentificationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {
    private Long id;

    @NotNull(message = "Identification type is required")
    @JsonProperty("identificationType")
    private IdentificationType identificationType;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String identificationNumber;

    @Email
    private String email;

    @Past
    private LocalDate birthDate;
}
