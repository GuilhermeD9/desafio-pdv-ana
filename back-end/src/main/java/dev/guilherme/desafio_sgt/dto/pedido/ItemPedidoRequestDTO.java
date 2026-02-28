package dev.guilherme.desafio_sgt.dto.pedido;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemPedidoRequestDTO(
        @NotNull(message = "O ID do produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        Integer quantidade,

        @Digits(integer = 10, fraction = 2)
        @Positive(message = "O valor unitário deve ser maior que zero.")
        BigDecimal valorUnitario,

        @Positive(message = "O desconto não pode ser negativo.")
        BigDecimal desconto
) {}
