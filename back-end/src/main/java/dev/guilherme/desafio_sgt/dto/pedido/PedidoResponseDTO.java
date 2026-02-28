package dev.guilherme.desafio_sgt.dto.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        Long clienteId,
        LocalDateTime dataPedido,
        BigDecimal valorTotal,
        List<ItemPedidoRequestDTO> itens
) {}
