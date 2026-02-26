package dev.guilherme.desafio_sgt.dto.produto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        Integer quantidade
) {}
