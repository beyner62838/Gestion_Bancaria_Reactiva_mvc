package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Dto.ClientDTO;
import com.example.Prueba_Tecnica.Entity.Client;
import com.example.Prueba_Tecnica.Entity.Enums.IdentificationType;
import com.example.Prueba_Tecnica.Repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = Client.builder()
                .id(1L)
                .identificationType(IdentificationType.CC)
                .identificationNumber("123")
                .firstName("Pepe")
                .lastName("Perez")
                .email("pepe@example.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .active(true)
                .build();
    }

    @Test
    void shouldCreateClientWhenNotExists() {
        ClientDTO dto = ClientDTO.builder()
                .identificationType(IdentificationType.CE)
                .identificationNumber("123")
                .firstName("Pepe")
                .lastName("Perez")
                .email("pepe@example.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        when(repository.existsByIdentificationNumber("123")).thenReturn(Mono.just(false));
        when(repository.existsByEmail("pepe@example.com")).thenReturn(Mono.just(false));
        when(repository.save(any(Client.class))).thenAnswer(inv -> {
            Client saved = inv.getArgument(0);
            saved.setId(1L);
            return Mono.just(saved);
        });

        StepVerifier.create(clientService.createClient(dto))
                .assertNext(created -> {
                    assertNotNull(created.getId());
                    assertEquals("123", created.getIdentificationNumber());
                    assertEquals("pepe@example.com", created.getEmail());
                })
                .verifyComplete();

        verify(repository, times(1)).save(any(Client.class));
    }

    @Test
    void shouldLogicalDeleteClient() {
        when(repository.findById(1L)).thenReturn(Mono.just(client));
        when(repository.save(any(Client.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(clientService.delete(1L))
                .verifyComplete();

        assertFalse(client.isActive());
        verify(repository, times(1)).save(any(Client.class));
    }
}