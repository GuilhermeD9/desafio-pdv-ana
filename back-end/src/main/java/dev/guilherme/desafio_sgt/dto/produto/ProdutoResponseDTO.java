package dev.guilherme.desafio_sgt.dto.produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        Integer quantidade,
        LocalDateTime dataCadastro
) {}
