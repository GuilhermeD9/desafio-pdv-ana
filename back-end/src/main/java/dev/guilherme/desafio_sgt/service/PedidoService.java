package dev.guilherme.desafio_sgt.service;

import dev.guilherme.desafio_sgt.dto.pedido.ItemPedidoRequestDTO;
import dev.guilherme.desafio_sgt.dto.pedido.ItemPedidoResponseDTO;
import dev.guilherme.desafio_sgt.dto.pedido.PedidoRequestDTO;
import dev.guilherme.desafio_sgt.dto.pedido.PedidoResponseDTO;
import dev.guilherme.desafio_sgt.model.ItemPedido;
import dev.guilherme.desafio_sgt.model.Pedido;
import dev.guilherme.desafio_sgt.model.Produto;
import dev.guilherme.desafio_sgt.repository.PedidoRepository;
import dev.guilherme.desafio_sgt.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PedidoResponseDTO cadastrar(PedidoRequestDTO pedidoDto) {

        Pedido pedido = new Pedido();
        pedido.setClienteId(pedidoDto.clienteId());
        BigDecimal valorTotal = BigDecimal.ZERO;
        List<ItemPedido> itensParaGuardar = new ArrayList<>();

        for (ItemPedidoRequestDTO item : pedidoDto.itens()) {
            Produto produto = produtoRepository.buscarPorId(item.produtoId());

            if (produto.getQuantidade() < item.quantidade()) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente para o produto '" + produto.getDescricao() +
                                "'. Disponível: " + produto.getQuantidade()
                );
            }

            BigDecimal desconto = item.desconto() != null ? item.desconto() : BigDecimal.ZERO;

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProdutoId(produto.getId());
            itemPedido.setQuantidade(item.quantidade());
            itemPedido.setValorUnitario(produto.getValor());
            itemPedido.setDesconto(desconto);

            BigDecimal subtotalItem = itemPedido.getValorUnitario()
                    .multiply(BigDecimal.valueOf(itemPedido.getQuantidade()))
                    .subtract(desconto);

            valorTotal = valorTotal.add(subtotalItem);
            itensParaGuardar.add(itemPedido);
        }

        pedido.setValorTotal(valorTotal);
        pedido.setItens(itensParaGuardar);

        Pedido salvo = pedidoRepository.cadastrar(pedido);

        return mapToResponse(salvo);
    }

    public List<PedidoResponseDTO> listarPorClienteId(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.listarPorClienteId(clienteId);

        for (Pedido pedido : pedidos) {
            List<ItemPedido> itens = pedidoRepository.buscarItensPorPedidoId(pedido.getId());
            pedido.setItens(itens);
        }

        return pedidos.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<PedidoResponseDTO> listarPorProdutoId(Long produtoId) {
        List<Pedido> pedidos = pedidoRepository.listarPorProdutoId(produtoId);

        for (Pedido pedido : pedidos) {
            List<ItemPedido> itens = pedidoRepository.buscarItensPorPedidoId(pedido.getId());
            pedido.setItens(itens);
        }

        return pedidos.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public BigDecimal buscarValorTotalPorCliente(Long clienteId) {
        return pedidoRepository.buscarValorTotalPorCliente(clienteId);
    }

    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.buscarPorId(id);

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado para o ID " + id);
        }

        List<ItemPedido> itens = pedidoRepository.buscarItensPorPedidoId(pedido.getId());
        pedido.setItens(itens);

        return mapToResponse(pedido);
    }

    public List<PedidoResponseDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas de início e fim são obrigatórias.");
        }

        LocalDateTime dtInicio = inicio.atStartOfDay();
        LocalDateTime dtFim = fim.atTime(LocalTime.MAX);

        List<Pedido> pedidos = pedidoRepository.buscarPorPeriodo(dtInicio, dtFim);

        for (Pedido pedido : pedidos) {
            List<ItemPedido> itens = pedidoRepository.buscarItensPorPedidoId(pedido.getId());
            pedido.setItens(itens);
        }

        return pedidoRepository.buscarPorPeriodo(dtInicio, dtFim).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PedidoResponseDTO mapToResponse(Pedido pedido) {
        List<ItemPedidoResponseDTO> itensResponse = pedido.getItens().stream()
                .map(item -> {
                    Produto produto = produtoRepository.buscarPorId(item.getProdutoId());

                    return new ItemPedidoResponseDTO(
                            item.getProdutoId(),
                            produto.getDescricao(),
                            item.getQuantidade(),
                            item.getValorUnitario(),
                            item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())).subtract(item.getDesconto())
                    );
                })
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getDataPedido(),
                pedido.getValorTotal(),
                itensResponse
        );
    }
}
