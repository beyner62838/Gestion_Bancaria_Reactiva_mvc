package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Dto.ClientDTO;
import com.example.Prueba_Tecnica.Entity.Client;
import com.example.Prueba_Tecnica.IService.IClientService;
import com.example.Prueba_Tecnica.Repository.ClientRepository;
import com.example.Prueba_Tecnica.Util.BusinessException;
import com.example.Prueba_Tecnica.Util.ClientMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private final ClientRepository repository;

    // ---- Interface (entity-level) ----
    @Override
    public Mono<Client> findByIdentificationNumber(String identificationNumber) {
        return repository.findByIdentificationNumber(identificationNumber)
                .filter(Client::isActive);
    }

    @Override
    public Mono<Boolean> existsByIdentificationNumber(String identificationNumber) {
        return repository.existsByIdentificationNumber(identificationNumber);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Flux<Client> findAll() {
        return repository.findAllByActiveTrue();
    }

    @Override
    public Mono<Client> findById(Long id) {
        return repository.findById(id)
                .filter(Client::isActive)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, ClientMessages.CLIENT_NOT_FOUND + id)));
    }

    @Override
    public Mono<Client> save(Client client) {
        LocalDateTime now = LocalDateTime.now();
        client.setId(null);
        client.setCreationDate(now);
        client.setModificationDate(now);
        client.setActive(true);
        return repository.save(client);
    }

    @Override
    public Mono<Client> update(Client incoming, Long id) {
        return findById(id)
                .flatMap(existing -> {
                    existing.setFirstName(incoming.getFirstName());
                    existing.setLastName(incoming.getLastName());
                    existing.setIdentificationType(incoming.getIdentificationType());
                    existing.setIdentificationNumber(incoming.getIdentificationNumber());
                    existing.setEmail(incoming.getEmail());
                    existing.setBirthDate(incoming.getBirthDate());
                    existing.setModificationDate(LocalDateTime.now());
                    return repository.save(existing);
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return findById(id)
                .flatMap(existing -> {
                    existing.setActive(false);
                    existing.setModificationDate(LocalDateTime.now());
                    return repository.save(existing).then();
                });
    }

    // ---- DTO-level methods used by Controller ----
    public Mono<ClientDTO> createClient(ClientDTO dto) {
        if (dto.getIdentificationType() == null) {
            return Mono.error(new BusinessException("Identification type is required"));
        }

        return Mono.zip(
                existsByIdentificationNumber(dto.getIdentificationNumber()),
                existsByEmail(dto.getEmail())
        ).flatMap(exists -> {
            if (Boolean.TRUE.equals(exists.getT1())) {
                return Mono.error(new BusinessException(ClientMessages.IDENTIFICATION_ALREADY_EXISTS));
            }
            if (Boolean.TRUE.equals(exists.getT2())) {
                return Mono.error(new BusinessException(ClientMessages.EMAIL_ALREADY_EXISTS));
            }

            Client entity = toEntity(dto);
            return save(entity).map(this::toDTO);
        });
    }

    public Flux<ClientDTO> getAllClients() {
        return findAll().map(this::toDTO);
    }

    public Mono<ClientDTO> getClientById(Long id) {
        return findById(id).map(this::toDTO);
    }

    public Mono<ClientDTO> updateClient(Long id, ClientDTO dto) {
        Client incoming = toEntity(dto);
        return update(incoming, id).map(this::toDTO);
    }

    // mapping helpers
    private ClientDTO toDTO(Client client) {
        return ClientDTO.builder()
                .id(client.getId())
                .identificationType(client.getIdentificationType())
                .identificationNumber(client.getIdentificationNumber())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .birthDate(client.getBirthDate())
                .build();
    }

    private Client toEntity(ClientDTO dto) {
        return Client.builder()
                .id(dto.getId())
                .identificationType(dto.getIdentificationType())
                .identificationNumber(dto.getIdentificationNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .birthDate(dto.getBirthDate())
                .active(true)
                .build();
    }
}
