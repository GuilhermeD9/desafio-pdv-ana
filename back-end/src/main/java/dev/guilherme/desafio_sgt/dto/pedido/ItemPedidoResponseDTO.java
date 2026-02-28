package dev.guilherme.desafio_sgt.dto.pedido;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO (
        Long produtoId,
        String descricao,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal subtotal
) {}
