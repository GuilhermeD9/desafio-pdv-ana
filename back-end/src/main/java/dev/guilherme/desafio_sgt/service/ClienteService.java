package dev.guilherme.desafio_sgt.service;

import dev.guilherme.desafio_sgt.model.Cliente;
import dev.guilherme.desafio_sgt.repository.ClienteRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public int cadastrar(Cliente cliente) {
        if (cliente == null || cliente.getNome() == null || cliente.getEmail() == null) {
            throw new IllegalArgumentException("Dados do cliente incompletos.");
        }

        return clienteRepository.cadastrar(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }

    public List<Cliente> buscar(String termo) {
        try {
            Long id = Long.parseLong(termo);
            Cliente cliente = clienteRepository.buscarPorId(id);
            return Collections.singletonList(cliente);
        } catch (NumberFormatException e) {
            return clienteRepository.buscarPorNome(termo);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
}
