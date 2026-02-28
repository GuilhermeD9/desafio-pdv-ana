package dev.guilherme.desafio_sgt.repository;

import dev.guilherme.desafio_sgt.model.Produto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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

    public Produto cadastrar(Produto produto) {
        String sql = "INSERT INTO produto (descricao, valor, quantidade) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, produto.getDescricao());
            ps.setBigDecimal(2, produto.getValor());
            ps.setInt(3, produto.getQuantidade());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            long idGerado = keyHolder.getKey().longValue();
            return buscarPorId(idGerado);
        }
        return produto;
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

    public boolean produtoExistente(String descricao) {
        String sql = "SELECT COUNT(*) FROM produto WHERE descricao = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, descricao);
        return Boolean.TRUE.equals(exists);
    }
}
