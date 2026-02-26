package dev.guilherme.desafio_sgt.service;

import dev.guilherme.desafio_sgt.dto.cliente.ClienteRequestDTO;
import dev.guilherme.desafio_sgt.dto.cliente.ClienteResponseDTO;
import dev.guilherme.desafio_sgt.model.Cliente;
import dev.guilherme.desafio_sgt.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponseDTO cadastrar(ClienteRequestDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.nome());
        cliente.setEmail(clienteDTO.email());

        Cliente salvo = clienteRepository.cadastrar(cliente);
        return mapToResponse(salvo);
    }

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.listarTodos().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.buscarPorId(id);
        return mapToResponse(cliente);
    }

    public List<ClienteResponseDTO> buscarPorNome(String nome) {
        return clienteRepository.buscarPorNome(nome).stream()
                .map(this::mapToResponse)
                .toList();
        }

    private ClienteResponseDTO mapToResponse(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDataCadastro()
        );
    }
}
