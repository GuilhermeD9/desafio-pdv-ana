package dev.guilherme.desafio_sgt.controller;

import dev.guilherme.desafio_sgt.model.Produto;
import dev.guilherme.desafio_sgt.service.ProdutoService;
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
    public ResponseEntity<String> cadastrar(@RequestBody Produto produto) {
        produtoService.cadastrar(produto);
        return ResponseEntity.status(201).body("Produto cadastrado com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Object> buscar(@RequestParam("termo") String termo) {
        Object resultado = produtoService.buscar(termo);

        if (resultado != null) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
