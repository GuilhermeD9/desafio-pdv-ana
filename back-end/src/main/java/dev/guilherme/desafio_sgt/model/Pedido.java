package dev.guilherme.desafio_sgt.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id;
    private Long clienteId;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
    }

    public Pedido(Long id, Long clienteId, LocalDateTime dataPedido, BigDecimal valorTotal, List<ItemPedido> itens) {
        this.id = id;
        this.clienteId = clienteId;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
}
