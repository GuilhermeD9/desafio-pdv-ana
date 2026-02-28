package dev.guilherme.desafio_sgt.repository;

import dev.guilherme.desafio_sgt.model.ItemPedido;
import dev.guilherme.desafio_sgt.model.Pedido;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class PedidoRepository {

    private final JdbcTemplate jdbcTemplate;

    public PedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Pedido> pedidoRowMapper = (rs, rowNum) -> {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getLong("id"));
        pedido.setClienteId(rs.getLong("cliente_id"));
        if (rs.getTimestamp("data_pedido") != null) {
            pedido.setDataPedido(rs.getTimestamp("data_pedido").toLocalDateTime());
        }
        pedido.setValorTotal(rs.getBigDecimal("valor_total"));
        return pedido;
    };

    private final RowMapper<ItemPedido> itemPedidoRowMapper = (rs, rowNum) -> {
        ItemPedido item = new ItemPedido();
        item.setId(rs.getLong("id"));
        item.setPedidoId(rs.getLong("pedido_id"));
        item.setProdutoId(rs.getLong("produto_id"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setValorUnitario(rs.getBigDecimal("valor_unitario"));
        item.setDesconto(rs.getBigDecimal("desconto"));
        return item;
    };

    public Pedido cadastrar(Pedido pedido) {
        String sql = "INSERT INTO pedido (cliente_id, data_pedido, valor_total) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, pedido.getClienteId());
            ps.setTimestamp(2, Timestamp.valueOf(pedido.getDataPedido() != null ? pedido.getDataPedido() : LocalDateTime.now()));
            ps.setBigDecimal(3, pedido.getValorTotal());
            return ps;
        }, keyHolder);

        Long pedidoId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        pedido.setId(pedidoId);

        String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade, valor_unitario, desconto) VALUES (?, ?, ?, ?, ?)";
        String sqlAtualizaEstoque = "UPDATE produto SET quantidade = quantidade - ? WHERE id = ?";

        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update(sqlItem,
                    pedidoId,
                    item.getProdutoId(),
                    item.getQuantidade(),
                    item.getValorUnitario(),
                    item.getDesconto());

            int linhasAfetadas = jdbcTemplate.update(sqlAtualizaEstoque, item.getQuantidade(), item.getProdutoId());

            if (linhasAfetadas == 0) {
                throw new RuntimeException("Falha ao atualizar estoque: Produto ID " + item.getProdutoId() + " não encontrado.");
            }
        }
        return buscarPorId(pedidoId);
    }

    public List<Pedido> listarPorClienteId(Long clienteId) {
        String sql = "SELECT id, cliente_id, data_pedido, valor_total FROM pedido WHERE cliente_id = ?";
        return jdbcTemplate.query(sql, pedidoRowMapper, clienteId);
    }

    public List<Pedido> listarPorProdutoId(Long produtoId) {
        String sql = """
                SELECT p.id, p.cliente_id, p.data_pedido, p.valor_total
                FROM pedido p
                INNER JOIN item_pedido ip ON p.id = ip.pedido_id
                WHERE ip.produto_id = ?
                """;
        return jdbcTemplate.query(sql, pedidoRowMapper, produtoId);
    }

    public BigDecimal buscarValorTotalPorCliente(Long clienteId) {
        String sql = "SELECT SUM(valor_total) FROM pedido WHERE cliente_id = ?";
        BigDecimal total = jdbcTemplate.queryForObject(sql, BigDecimal.class, clienteId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Pedido buscarPorId(Long id) {
        String sql = "SELECT id, cliente_id, data_pedido, valor_total FROM pedido WHERE id = ?";
        Pedido pedido = jdbcTemplate.queryForObject(sql, pedidoRowMapper, id);

        if (pedido != null) {
            String sqlItens = "SELECT id, pedido_id, produto_id, quantidade, valor_unitario, desconto FROM item_pedido WHERE pedido_id = ?";
            List<ItemPedido> itens = jdbcTemplate.query(sqlItens, itemPedidoRowMapper, id);
            pedido.setItens(itens);
        }

        return pedido;
    }

    public List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT id, cliente_id, data_pedido, valor_total FROM pedido WHERE data_pedido BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, pedidoRowMapper, Timestamp.valueOf(inicio), Timestamp.valueOf(fim));
    }
}
