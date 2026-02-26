package dev.guilherme.desafio_sgt.controller;

import dev.guilherme.desafio_sgt.dto.cliente.ClienteRequestDTO;
import dev.guilherme.desafio_sgt.dto.cliente.ClienteResponseDTO;
import dev.guilherme.desafio_sgt.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(@RequestBody @Valid ClienteRequestDTO cliente) {
        return ResponseEntity.status(201).body(clienteService.cadastrar(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/busca")
    public ResponseEntity<Object> buscar(@RequestParam("termo") String termo) {
        Object resultado = clienteService.buscar(termo);

        if (resultado != null) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}