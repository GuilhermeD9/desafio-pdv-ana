package dev.guilherme.desafio_sgt.dto.produto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "A descrição do produto é obrigatória.")
        String descricao,

        @NotNull(message = "O valor é obrigatório.")
        @Digits(integer = 10, fraction = 2)
        @Positive(message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "A quantidade inicial é obrigatória.")
        @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
        Integer quantidade
) {}
