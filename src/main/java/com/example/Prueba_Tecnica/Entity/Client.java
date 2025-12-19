package com.example.Prueba_Tecnica.Entity;

import com.example.Prueba_Tecnica.Entity.Enums.IdentificationType;
import com.fasterxml.jackson.core.JsonToken;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    private Long id;

    @NotNull(message = "Identification type is required")
    @Column("identification_type")
    private IdentificationType identificationType;

    @NotBlank(message = "Identification number is required")
    @Column("identification_number")
    private String identificationNumber;

    @NotBlank(message = "First name is required")
    @Size(min = 2, message = "First name must have at least 2 characters")
    @Column("first_name")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, message = "Last name must have at least 2 characters")
    @Column("last_name")
    private String lastName;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Column("email")
    private String email;

    @Past(message = "Birthdate must be in the past")
    @NotNull(message = "Birth date is required")
    @Column("birth_date")
    private LocalDate birthDate;

    @Column("creation_date")
    private LocalDateTime creationDate;

    @Column("modification_date")
    private LocalDateTime modificationDate;

    @Column("active")
    private boolean active = true;

}
