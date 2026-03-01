package dev.guilherme.desafio_sgt.service;

import dev.guilherme.desafio_sgt.dto.produto.ProdutoRequestDTO;
import dev.guilherme.desafio_sgt.dto.produto.ProdutoResponseDTO;
import dev.guilherme.desafio_sgt.model.Produto;
import dev.guilherme.desafio_sgt.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void cadastrar_quandoDescricaoJaExiste_deveLancarExcecao() {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Mochila", BigDecimal.TEN, 10);

        when(produtoRepository.produtoExistente(request.descricao())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> produtoService.cadastrar(request));

        assertEquals("O produto fornecido já existe!", ex.getMessage());
        verify(produtoRepository).produtoExistente(request.descricao());
        verify(produtoRepository, never()).cadastrar(any(Produto.class));
    }

    @Test
    void cadastrar_quandoProdutoNaoExiste_deveCadastrarERetornarResponse() {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Mochila", BigDecimal.TEN, 10);

        Produto salvo = new Produto(1L, "Mochila", BigDecimal.TEN, 10, LocalDateTime.now());

        when(produtoRepository.produtoExistente(request.descricao())).thenReturn(false);
        when(produtoRepository.cadastrar(any(Produto.class))).thenReturn(salvo);

        ProdutoResponseDTO response = produtoService.cadastrar(request);

        assertNotNull(response);
        assertEquals(salvo.getId(), response.id());
        assertEquals(salvo.getDescricao(), response.descricao());
        assertEquals(salvo.getValor(), response.valor());
        assertEquals(salvo.getQuantidade(), response.quantidade());
        assertEquals(salvo.getDataCadastro(), response.dataCadastro());

        verify(produtoRepository).produtoExistente(request.descricao());
        verify(produtoRepository).cadastrar(any(Produto.class));
    }

    @Test
    void listarTodos_deveMapearParaResponseDTO() {
        Produto p1 = new Produto(1L, "Mochila", BigDecimal.TEN, 10, LocalDateTime.now());
        Produto p2 = new Produto(2L, "Estojo", BigDecimal.TWO, 5, LocalDateTime.now());

        when(produtoRepository.listarTodos()).thenReturn(List.of(p1, p2));

        List<ProdutoResponseDTO> result = produtoService.listarTodos();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(p1.getId(), result.get(0).id());
        assertEquals(p2.getId(), result.get(1).id());
        verify(produtoRepository).listarTodos();
    }

    @Test
    void buscarPorId_deveRetornarResponseDTO() {
        Produto produto = new Produto(10L, "Mochila", BigDecimal.TEN, 10, LocalDateTime.now());

        when(produtoRepository.buscarPorId(10L)).thenReturn(produto);

        ProdutoResponseDTO result = produtoService.buscarPorId(10L);

        assertNotNull(result);
        assertEquals(produto.getId(), result.id());
        assertEquals(produto.getDescricao(), result.descricao());
        assertEquals(produto.getValor(), result.valor());
        assertEquals(produto.getQuantidade(), result.quantidade());
        assertEquals(produto.getDataCadastro(), result.dataCadastro());
        verify(produtoRepository).buscarPorId(10L);
    }

    @Test
    void buscarPorDescricao_deveRetornarListaDeResponseDTO() {
        Produto p1 = new Produto(1L, "Mochila Rosa", BigDecimal.TEN, 10, LocalDateTime.now());
        Produto p2 = new Produto(2L, "Mochila Verde", BigDecimal.TWO, 5, LocalDateTime.now());

        when(produtoRepository.buscarPorDescricao("Mochila")).thenReturn(List.of(p1, p2));

        List<ProdutoResponseDTO> result = produtoService.buscarPorDescricao("Mochila");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(p1.getId(), result.get(0).id());
        assertEquals(p2.getId(), result.get(1).id());
        verify(produtoRepository).buscarPorDescricao("Mochila");
    }
}
