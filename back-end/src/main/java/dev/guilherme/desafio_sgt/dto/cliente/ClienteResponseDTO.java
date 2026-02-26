package dev.guilherme.desafio_sgt.dto.cliente;

import java.time.LocalDateTime;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String email,
        LocalDateTime dataCadastro
) {}
