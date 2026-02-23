package dev.guilherme.desafio_sgt.repository;

import dev.guilherme.desafio_sgt.model.Produto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProdutoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProdutoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Produto> produtoRowMapper = (rs, rowNum) -> {
        Produto produto = new Produto();
        produto.setId(rs.getLong("id"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setValor(rs.getBigDecimal("valor"));
        produto.setQuantidade(rs.getInt("quantidade"));

        if (rs.getTimestamp("data_cadastro") != null) {
            produto.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        }
        return produto;
    };

    public int cadastrar(Produto produto) {
        String sql = "INSERT INTO produto (descricao, valor, quantidade) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, produto.getDescricao(), produto.getValor(), produto.getQuantidade());
    }

    public List<Produto> listarTodos() {
        String sql = "SELECT id, descricao, valor, quantidade, data_cadastro FROM produto";
        return jdbcTemplate.query(sql, produtoRowMapper);
    }

    public Produto buscarPorId(Long id) {
        String sql = "SELECT id, descricao, valor, quantidade, data_cadastro FROM produto WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, produtoRowMapper, id);
    }

    public List<Produto> buscarPorDescricao(String descricao) {
        String sql = "SELECT id, descricao, valor, quantidade, data_cadastro FROM produto WHERE descricao ILIKE ?";
        return jdbcTemplate.query(sql, produtoRowMapper, "%" + descricao + "%");
    }
}
