package dev.guilherme.desafio_sgt.service;

import dev.guilherme.desafio_sgt.dto.pedido.ItemPedidoRequestDTO;
import dev.guilherme.desafio_sgt.dto.pedido.PedidoRequestDTO;
import dev.guilherme.desafio_sgt.dto.pedido.PedidoResponseDTO;
import dev.guilherme.desafio_sgt.model.ItemPedido;
import dev.guilherme.desafio_sgt.model.Pedido;
import dev.guilherme.desafio_sgt.model.Produto;
import dev.guilherme.desafio_sgt.repository.PedidoRepository;
import dev.guilherme.desafio_sgt.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void cadastrar_quandoEstoqueInsuficiente_deveLancarExcecao() {
        ItemPedidoRequestDTO item = new ItemPedidoRequestDTO(1L, 10, BigDecimal.TEN, BigDecimal.ZERO);
        PedidoRequestDTO request = new PedidoRequestDTO(1L, List.of(item));

        Produto produto = new Produto(1L, "Mochila", BigDecimal.TEN, 5, LocalDateTime.now());

        when(produtoRepository.buscarPorId(1L)).thenReturn(produto);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pedidoService.cadastrar(request));

        assertTrue(ex.getMessage().contains("Estoque insuficiente"));
        verify(produtoRepository).buscarPorId(1L);
        verify(pedidoRepository, never()).cadastrar(any(Pedido.class));
    }

    @Test
    void cadastrar_quandoOk_deveCadastrarCalcularTotalERetornarResponse() {
        ItemPedidoRequestDTO item1 = new ItemPedidoRequestDTO(1L, 2, null, BigDecimal.ONE);
        ItemPedidoRequestDTO item2 = new ItemPedidoRequestDTO(2L, 1, BigDecimal.valueOf(20), null);
        PedidoRequestDTO request = new PedidoRequestDTO(10L, List.of(item1, item2));

        Produto p1 = new Produto(1L, "P1", BigDecimal.TEN, 10, LocalDateTime.now());
        Produto p2 = new Produto(2L, "P2", BigDecimal.valueOf(30), 10, LocalDateTime.now());

        when(produtoRepository.buscarPorId(1L)).thenReturn(p1);
        when(produtoRepository.buscarPorId(2L)).thenReturn(p2);

        Pedido salvo = new Pedido();
        salvo.setId(100L);
        salvo.setClienteId(10L);
        salvo.setDataPedido(LocalDateTime.now());
        salvo.setValorTotal(BigDecimal.valueOf(39));

        ItemPedido ip1 = new ItemPedido();
        ip1.setProdutoId(1L);
        ip1.setQuantidade(2);
        ip1.setValorUnitario(BigDecimal.TEN);
        ip1.setDesconto(BigDecimal.ONE);

        ItemPedido ip2 = new ItemPedido();
        ip2.setProdutoId(2L);
        ip2.setQuantidade(1);
        ip2.setValorUnitario(BigDecimal.valueOf(20));
        ip2.setDesconto(BigDecimal.ZERO);

        salvo.setItens(List.of(ip1, ip2));

        when(pedidoRepository.cadastrar(any(Pedido.class))).thenReturn(salvo);

        PedidoResponseDTO response = pedidoService.cadastrar(request);

        assertNotNull(response);
        assertEquals(100L, response.id());
        assertEquals(10L, response.clienteId());
        assertEquals(BigDecimal.valueOf(39), response.valorTotal());
        assertNotNull(response.itens());
        assertEquals(2, response.itens().size());

        verify(pedidoRepository).cadastrar(any(Pedido.class));
        verify(produtoRepository, atLeastOnce()).buscarPorId(anyLong());
    }

    @Test
    void listarPorClienteId_deveCarregarItensEMapear() {
        Pedido p1 = new Pedido();
        p1.setId(1L);
        p1.setClienteId(10L);
        p1.setValorTotal(BigDecimal.TEN);

        ItemPedido item = new ItemPedido();
        item.setProdutoId(1L);
        item.setQuantidade(1);
        item.setValorUnitario(BigDecimal.TEN);
        item.setDesconto(BigDecimal.ZERO);

        when(pedidoRepository.listarPorClienteId(10L)).thenReturn(List.of(p1));
        when(pedidoRepository.buscarItensPorPedidoId(1L)).thenReturn(List.of(item));
        when(produtoRepository.buscarPorId(1L)).thenReturn(new Produto(1L, "P1", BigDecimal.TEN, 10, LocalDateTime.now()));

        List<PedidoResponseDTO> result = pedidoService.listarPorClienteId(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals(1, result.getFirst().itens().size());

        verify(pedidoRepository).listarPorClienteId(10L);
        verify(pedidoRepository).buscarItensPorPedidoId(1L);
    }

    @Test
    void listarPorProdutoId_deveCarregarItensEMapear() {
        Pedido p1 = new Pedido();
        p1.setId(1L);
        p1.setClienteId(10L);
        p1.setValorTotal(BigDecimal.TEN);

        ItemPedido item = new ItemPedido();
        item.setProdutoId(1L);
        item.setQuantidade(1);
        item.setValorUnitario(BigDecimal.TEN);
        item.setDesconto(BigDecimal.ZERO);

        when(pedidoRepository.listarPorProdutoId(1L)).thenReturn(List.of(p1));
        when(pedidoRepository.buscarItensPorPedidoId(1L)).thenReturn(List.of(item));
        when(produtoRepository.buscarPorId(1L)).thenReturn(new Produto(1L, "P1", BigDecimal.TEN, 10, LocalDateTime.now()));

        List<PedidoResponseDTO> result = pedidoService.listarPorProdutoId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals(1, result.getFirst().itens().size());

        verify(pedidoRepository).listarPorProdutoId(1L);
        verify(pedidoRepository).buscarItensPorPedidoId(1L);
    }

    @Test
    void buscarValorTotalPorCliente_deveDelegarAoRepository() {
        when(pedidoRepository.buscarValorTotalPorCliente(10L)).thenReturn(BigDecimal.valueOf(123));

        BigDecimal result = pedidoService.buscarValorTotalPorCliente(10L);

        assertEquals(BigDecimal.valueOf(123), result);
        verify(pedidoRepository).buscarValorTotalPorCliente(10L);
    }

    @Test
    void buscarPorId_quandoNaoEncontrado_deveLancarExcecao() {
        when(pedidoRepository.buscarPorId(99L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> pedidoService.buscarPorId(99L));

        verify(pedidoRepository).buscarPorId(99L);
        verify(pedidoRepository, never()).buscarItensPorPedidoId(anyLong());
    }

    @Test
    void buscarPorId_quandoEncontrado_deveCarregarItensEMapear() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setClienteId(10L);
        pedido.setValorTotal(BigDecimal.TEN);

        ItemPedido item = new ItemPedido();
        item.setProdutoId(1L);
        item.setQuantidade(1);
        item.setValorUnitario(BigDecimal.TEN);
        item.setDesconto(BigDecimal.ZERO);

        when(pedidoRepository.buscarPorId(1L)).thenReturn(pedido);
        when(pedidoRepository.buscarItensPorPedidoId(1L)).thenReturn(List.of(item));
        when(produtoRepository.buscarPorId(1L)).thenReturn(new Produto(1L, "P1", BigDecimal.TEN, 10, LocalDateTime.now()));

        PedidoResponseDTO result = pedidoService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(1, result.itens().size());

        verify(pedidoRepository).buscarPorId(1L);
        verify(pedidoRepository).buscarItensPorPedidoId(1L);
    }

    @Test
    void buscarPorPeriodo_quandoDatasNulas_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> pedidoService.buscarPorPeriodo(null, LocalDate.now()));
        assertThrows(IllegalArgumentException.class, () -> pedidoService.buscarPorPeriodo(LocalDate.now(), null));

        verifyNoInteractions(pedidoRepository);
        verifyNoInteractions(produtoRepository);
    }

    @Test
    void buscarPorPeriodo_deveCarregarItensEMapear() {
        Pedido p1 = new Pedido();
        p1.setId(1L);
        p1.setClienteId(10L);
        p1.setValorTotal(BigDecimal.TEN);

        ItemPedido item = new ItemPedido();
        item.setProdutoId(1L);
        item.setQuantidade(1);
        item.setValorUnitario(BigDecimal.TEN);
        item.setDesconto(null);

        when(pedidoRepository.buscarPorPeriodo(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(p1));
        when(pedidoRepository.buscarItensPorPedidoId(1L)).thenReturn(List.of(item));
        when(produtoRepository.buscarPorId(1L)).thenReturn(new Produto(1L, "P1", BigDecimal.TEN, 10, LocalDateTime.now()));

        List<PedidoResponseDTO> result = pedidoService.buscarPorPeriodo(LocalDate.now().minusDays(1), LocalDate.now());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals(1, result.getFirst().itens().size());

        verify(pedidoRepository).buscarPorPeriodo(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(pedidoRepository).buscarItensPorPedidoId(1L);
    }
}
