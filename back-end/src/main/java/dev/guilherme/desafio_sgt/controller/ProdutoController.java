package dev.guilherme.desafio_sgt.controller;

import dev.guilherme.desafio_sgt.dto.produto.ProdutoRequestDTO;
import dev.guilherme.desafio_sgt.dto.produto.ProdutoResponseDTO;
import dev.guilherme.desafio_sgt.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@RequestBody @Valid ProdutoRequestDTO produto) {
        return ResponseEntity.status(201).body(produtoService.cadastrar(produto));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Object> buscar(@RequestParam(required = false) Long id,
                                         @RequestParam(required = false) String descricao) {
        if (id != null && descricao == null) {
            return ResponseEntity.ok(produtoService.buscarPorId(id));
        } else if (id == null && descricao != null){
            return ResponseEntity.ok(produtoService.buscarPorDescricao(descricao));
        }

        return ResponseEntity.badRequest().body("Algum dos campos devem ser preenchidos");
    }
}
