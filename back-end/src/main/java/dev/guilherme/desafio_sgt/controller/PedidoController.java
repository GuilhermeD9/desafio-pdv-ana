package dev.guilherme.desafio_sgt.controller;

import dev.guilherme.desafio_sgt.model.Pedido;
import dev.guilherme.desafio_sgt.service.PedidoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> cadastrar(@RequestBody Pedido pedido) {
        return ResponseEntity.status(201).body(pedidoService.cadastrar(pedido));
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<Pedido>> listarPorClienteId(@RequestParam("clienteId") Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPorClienteId(clienteId));
    }

    @GetMapping("/produto")
    public ResponseEntity<List<Pedido>> listarPorProdutoId(@RequestParam("produtoId")Long produtoId) {
        return ResponseEntity.ok(pedidoService.listarPorProdutoId(produtoId));
    }

    @GetMapping("/cliente/valorTotal")
    public ResponseEntity<BigDecimal> buscarValorTotalPorCliente(@RequestParam("clienteId") Long clienteId) {
        return ResponseEntity.ok(pedidoService.buscarValorTotalPorCliente(clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping("/buscar/periodo")
    public ResponseEntity<List<Pedido>> buscarPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(pedidoService.buscarPorPeriodo(inicio, fim));
    }
}
