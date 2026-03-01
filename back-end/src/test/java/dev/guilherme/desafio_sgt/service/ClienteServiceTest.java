package dev.guilherme.desafio_sgt.service;

import dev.guilherme.desafio_sgt.dto.cliente.ClienteRequestDTO;
import dev.guilherme.desafio_sgt.dto.cliente.ClienteResponseDTO;
import dev.guilherme.desafio_sgt.model.Cliente;
import dev.guilherme.desafio_sgt.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void cadastrar_quandoEmailJaExiste_deveLancarExcecao() {
        ClienteRequestDTO request = new ClienteRequestDTO("Fulano", "fulano@email.com");

        when(clienteRepository.emailExistente(request.email())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clienteService.cadastrar(request));

        assertEquals("O email fornecido já existe!", ex.getMessage());
        verify(clienteRepository).emailExistente(request.email());
        verify(clienteRepository, never()).cadastrar(any(Cliente.class));
    }

    @Test
    void cadastrar_quandoEmailNaoExiste_deveCadastrarERetornarResponse() {
        ClienteRequestDTO request = new ClienteRequestDTO("Fulano", "fulano@email.com");

        Cliente salvo = new Cliente(1L, "Fulano", "fulano@email.com", LocalDateTime.now());

        when(clienteRepository.emailExistente(request.email())).thenReturn(false);
        when(clienteRepository.cadastrar(any(Cliente.class))).thenReturn(salvo);

        ClienteResponseDTO response = clienteService.cadastrar(request);

        assertNotNull(response);
        assertEquals(salvo.getId(), response.id());
        assertEquals(salvo.getNome(), response.nome());
        assertEquals(salvo.getEmail(), response.email());
        assertEquals(salvo.getDataCadastro(), response.dataCadastro());

        verify(clienteRepository).emailExistente(request.email());
        verify(clienteRepository).cadastrar(any(Cliente.class));
    }

    @Test
    void listarTodos_deveMapearParaResponseDTO() {
        Cliente c1 = new Cliente(1L, "A", "a@email.com", LocalDateTime.now());
        Cliente c2 = new Cliente(2L, "B", "b@email.com", LocalDateTime.now());

        when(clienteRepository.listarTodos()).thenReturn(List.of(c1, c2));

        List<ClienteResponseDTO> result = clienteService.listarTodos();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(c1.getId(), result.get(0).id());
        assertEquals(c2.getId(), result.get(1).id());
        verify(clienteRepository).listarTodos();
    }

    @Test
    void buscarPorId_deveRetornarResponseDTO() {
        Cliente cliente = new Cliente(10L, "Fulano", "fulano@email.com", LocalDateTime.now());

        when(clienteRepository.buscarPorId(10L)).thenReturn(cliente);

        ClienteResponseDTO result = clienteService.buscarPorId(10L);

        assertNotNull(result);
        assertEquals(cliente.getId(), result.id());
        assertEquals(cliente.getNome(), result.nome());
        assertEquals(cliente.getEmail(), result.email());
        assertEquals(cliente.getDataCadastro(), result.dataCadastro());
        verify(clienteRepository).buscarPorId(10L);
    }

    @Test
    void buscarPorNome_deveRetornarListaDeResponseDTO() {
        Cliente c1 = new Cliente(1L, "Ana", "ana@email.com", LocalDateTime.now());
        Cliente c2 = new Cliente(2L, "Ana Paula", "anapaula@email.com", LocalDateTime.now());

        when(clienteRepository.buscarPorNome("Ana")).thenReturn(List.of(c1, c2));

        List<ClienteResponseDTO> result = clienteService.buscarPorNome("Ana");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(c1.getId(), result.get(0).id());
        assertEquals(c2.getId(), result.get(1).id());
        verify(clienteRepository).buscarPorNome("Ana");
    }
}
