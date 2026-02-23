package dev.guilherme.desafio_sgt.service;

import dev.guilherme.desafio_sgt.model.ItemPedido;
import dev.guilherme.desafio_sgt.model.Pedido;
import dev.guilherme.desafio_sgt.model.Produto;
import dev.guilherme.desafio_sgt.repository.PedidoRepository;
import dev.guilherme.desafio_sgt.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public Pedido cadastrar(Pedido pedido) {
        if (pedido == null || pedido.getClienteId() == null || pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("Dados do pedido incompletos.");
        }

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtoRepository.buscarPorId(item.getProdutoId());
            if (produto == null) {
                throw new IllegalArgumentException("Produto com ID " + item.getProdutoId() + " não encontrado.");
            }

            if (produto.getQuantidade() < item.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getDescricao());
            }

            item.setValorUnitario(produto.getValor());

            if (item.getDesconto() == null) {
                item.setDesconto(BigDecimal.ZERO);
            }

            BigDecimal subtotalItem = item.getValorUnitario()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()))
                    .subtract(item.getDesconto());

            valorTotal = valorTotal.add(subtotalItem);
        }

        pedido.setValorTotal(valorTotal);
        return pedidoRepository.cadastrar(pedido);
    }

    public List<Pedido> listarPorClienteId(Long clienteId) {
        return pedidoRepository.listarPorClienteId(clienteId);
    }

    public List<Pedido> listarPorProdutoId(Long produtoId) {
        return pedidoRepository.listarPorProdutoId(produtoId);
    }

    public BigDecimal buscarValorTotalPorCliente(Long clienteId) {
        return pedidoRepository.buscarValorTotalPorCliente(clienteId);
    }

    public Pedido buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.buscarPorId(id);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado para o ID " + id);
        }
        return pedido;
    }

    public List<Pedido> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas de início e fim são obrigatórias.");
        }

        LocalDateTime dtInicio = inicio.atStartOfDay();
        LocalDateTime dtFim = fim.atTime(LocalTime.MAX);

        return pedidoRepository.buscarPorPeriodo(dtInicio, dtFim);
    }
}
