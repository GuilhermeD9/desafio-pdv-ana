package dev.guilherme.desafio_sgt.service;

import dev.guilherme.desafio_sgt.model.Produto;
import dev.guilherme.desafio_sgt.repository.ProdutoRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public int cadastrar(Produto produto) {
        if (produto == null || produto.getDescricao() == null || produto.getValor() == null || produto.getQuantidade() == null) {
            throw new IllegalArgumentException("Dados do produto incompletos.");
        }

        return produtoRepository.cadastrar(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.listarTodos();
    }

    public List<Produto> buscar(String termo) {
        try {
            Long id = Long.parseLong(termo);
            Produto produto = produtoRepository.buscarPorId(id);
            return Collections.singletonList(produto);
        } catch (NumberFormatException e) {
            return produtoRepository.buscarPorDescricao(termo);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
}
