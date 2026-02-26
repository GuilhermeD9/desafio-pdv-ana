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
        return new ClienteResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getDataCadastro());
    }

    public List<ClienteResponseDTO> listarTodos() {
        List<Cliente> clientes = clienteRepository.listarTodos();
        return clientes.stream().map(cliente -> new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDataCadastro()
        )).toList();
    }

    public List<ClienteResponseDTO> buscar(String termo) {
        try {
            Long id = Long.parseLong(termo);
            Cliente cliente = clienteRepository.buscarPorId(id);

            ClienteResponseDTO dto = new ClienteResponseDTO(
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getEmail(),
                    cliente.getDataCadastro()
            );
            return List.of(dto);

        } catch (NumberFormatException e) {
            List<Cliente> clientes = clienteRepository.buscarPorNome(termo);

            return clientes.stream()
                    .map(c -> new ClienteResponseDTO(
                            c.getId(),
                            c.getNome(),
                            c.getEmail(),
                            c.getDataCadastro()
                    ))
                    .toList();
        }
    }
}
