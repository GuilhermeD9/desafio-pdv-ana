package dev.guilherme.desafio_sgt.service;

import dev.guilherme.desafio_sgt.dto.produto.ProdutoResponseDTO;
import dev.guilherme.desafio_sgt.dto.produto.ProdutoResquetDTO;
import dev.guilherme.desafio_sgt.model.Produto;
import dev.guilherme.desafio_sgt.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoResponseDTO cadastrar(ProdutoResquetDTO produtoDTO) {
        if (produtoRepository.produtoExistente(produtoDTO.descricao())) {
            throw new IllegalArgumentException("O produto fornecido já existe!");
        }

        Produto produto = new Produto();
        produto.setDescricao(produtoDTO.descricao());
        produto.setValor(produtoDTO.valor());
        produto.setQuantidade(produtoDTO.quantidade());

        Produto salvo = produtoRepository.cadastrar(produto);
        return mapToResponse(salvo);
    }

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.listarTodos().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.buscarPorId(id);
        return mapToResponse(produto);
    }

    public List<ProdutoResponseDTO> buscarPorDescricao(String descricao) {
        return produtoRepository.buscarPorDescricao(descricao).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProdutoResponseDTO mapToResponse(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getDescricao(),
                produto.getValor(),
                produto.getQuantidade(),
                produto.getDataCadastro()
        );
    }
}
