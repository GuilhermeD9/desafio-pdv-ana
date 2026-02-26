package dev.guilherme.desafio_sgt.controller;

import dev.guilherme.desafio_sgt.dto.cliente.ClienteRequestDTO;
import dev.guilherme.desafio_sgt.dto.cliente.ClienteResponseDTO;
import dev.guilherme.desafio_sgt.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> buscar(@RequestParam(required = false) Long id,
                                         @RequestParam(required = false) String nome) {
        if (id != null && nome == null) {
            return ResponseEntity.ok(clienteService.buscarPorId(id));
        } else if (id == null && nome != null) {
            return ResponseEntity.ok(clienteService.buscarPorNome(nome));
        }

        return ResponseEntity.badRequest().body("Apenas um dos campos devem ser preenchidos");
    }
}