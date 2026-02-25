package dev.guilherme.desafio_sgt.repository;

import dev.guilherme.desafio_sgt.model.Cliente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClienteRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Cliente> clienteRowMapper = (rs, rowNum) -> {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));

        if (rs.getTimestamp("data_cadastro") != null) {
            cliente.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        }
        return cliente;
    };

    public int cadastrar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, email) VALUES (?, ?)";
        return jdbcTemplate.update(sql, cliente.getNome(), cliente.getEmail());
    }

    public List<Cliente> listarTodos() {
        String sql = "SELECT id, nome, email, data_cadastro FROM cliente ORDER BY nome";
        return jdbcTemplate.query(sql, clienteRowMapper);
    }

    public Cliente buscarPorId(Long id) {
        String sql = "SELECT id, nome, email, data_cadastro FROM cliente WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, clienteRowMapper, id);
    }

    public List<Cliente> buscarPorNome(String nome) {
        String sql = "SELECT id, nome, email, data_cadastro FROM cliente WHERE nome ILIKE ?";
        return jdbcTemplate.query(sql, clienteRowMapper, "%" + nome + "%");
    }
}
