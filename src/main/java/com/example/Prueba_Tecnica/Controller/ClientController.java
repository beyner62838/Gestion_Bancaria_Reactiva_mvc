package com.example.Prueba_Tecnica.Controller;

import com.example.Prueba_Tecnica.Dto.ClientDTO;
import com.example.Prueba_Tecnica.Service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientS;

    @PostMapping
    public Mono<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        return clientS.createClient(clientDTO);
    }

    @GetMapping
    public Flux<ClientDTO> getAllClients() {
        return clientS.getAllClients();
    }

    @GetMapping("/{id}")
    public Mono<ClientDTO> getClientById(@PathVariable Long id) {
        return clientS.getClientById(id);
    }

    @PutMapping("/{id}")
    public Mono<ClientDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
        return clientS.updateClient(id, clientDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteClient(@PathVariable Long id) {
        return clientS.delete(id).thenReturn(ResponseEntity.noContent().build());
    }
}
