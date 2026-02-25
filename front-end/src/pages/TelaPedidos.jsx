import { useState, useEffect } from 'react';
import { Table, Button, Form, Card, Row, Col } from 'react-bootstrap';
import Swal from 'sweetalert2';
import api from '../api/api';

function TelaPedidos() {
  const [clientes, setClientes] = useState([]);
  const [produtos, setProdutos] = useState([]);

  const [clienteId, setClienteId] = useState('');
  const [produtoId, setProdutoId] = useState('');
  const [quantidade, setQuantidade] = useState(1);
  const [desconto, setDesconto] = useState(0);

  const [carrinho, setCarrinho] = useState([]);

  useEffect(() => {
    carregarDadosBase();
  }, []);

  const carregarDadosBase = async () => {
    try {
      const respClientes = await api.get('/clientes');
      const respProdutos = await api.get('/produtos');
      setClientes(respClientes.data);
      setProdutos(respProdutos.data);
    } catch (error) {
      console.error("Erro ao buscar dados:", error);
      Swal.fire({ icon: 'error', title: 'Erro', text: 'Falha ao carregar clientes ou produtos.' });
    }
  };

  const adicionarAoCarrinho = () => {
    if (!produtoId || quantidade <= 0) {
      Swal.fire({ icon: 'warning', title: 'Atenção', text: 'Selecione um produto e uma quantidade válida.' });
      return;
    }

    const produtoSelecionado = produtos.find(p => p.id === parseInt(produtoId));
    const qtdDesejada = parseInt(quantidade);
    const indexExistente = carrinho.findIndex(item => item.produtoId === produtoSelecionado.id);
    const qtdNoCarrinho = indexExistente >= 0 ? carrinho[indexExistente].quantidade : 0;

    if (qtdDesejada + qtdNoCarrinho > produtoSelecionado.quantidade) {
        Swal.fire({ 
        icon: 'error', 
        title: 'Estoque Insuficiente!', 
        text: `Você tentou adicionar ${qtdDesejada + qtdNoCarrinho} unidades, mas temos apenas ${produtoSelecionado.quantidade} de '${produtoSelecionado.descricao}' em estoque.` 
      });
      return;
    }

    if (indexExistente >= 0) {
        const novoCarrinho = [...carrinho];
        novoCarrinho[indexExistente].quantidade += qtdDesejada;
        novoCarrinho[indexExistente].desconto += (parseFloat(desconto) || 0);
        setCarrinho(novoCarrinho);
    } else {
        const novoItem = {
        produtoId: produtoSelecionado.id,
        descricao: produtoSelecionado.descricao,
        valorUnitario: produtoSelecionado.valor,
        quantidade: parseInt(quantidade),
        desconto: parseFloat(desconto) || 0
        };
        setCarrinho([...carrinho, novoItem]);
    }
    setProdutoId('');
    setQuantidade(1);
    setDesconto(0);
  };

  const removerDoCarrinho = (indexParaRemover) => {
    const carrinhoAtualizado = carrinho.filter((_, index) => index !== indexParaRemover);
    setCarrinho(carrinhoAtualizado);
  };

  const finalizarPedido = async () => {
    if (!clienteId) {
      Swal.fire({ icon: 'warning', title: 'Atenção', text: 'Selecione um cliente para o pedido.' });
      return;
    }
    if (carrinho.length === 0) {
      Swal.fire({ icon: 'warning', title: 'Atenção', text: 'O carrinho está vazio!' });
      return;
    }

    const payload = {
      clienteId: parseInt(clienteId),
      itens: carrinho.map(item => ({
        produtoId: item.produtoId,
        quantidade: item.quantidade,
        desconto: item.desconto
      }))
    };

    try {
      await api.post('/pedidos', payload);
      
      Swal.fire({
        icon: 'success',
        title: 'Pedido Salvo!',
        text: 'O pedido foi criado e o estoque atualizado com sucesso.',
        timer: 2500,
        showConfirmButton: false
      });

      setClienteId('');
      setCarrinho([]);
      carregarDadosBase();
    } catch (error) {
      console.error("Erro ao salvar pedido:", error);
      Swal.fire({ icon: 'error', title: 'Ops...', text: error.response?.data || 'Erro ao salvar o pedido. Verifique o estoque.' });
    }
  };

  return (
    <div>
      <h2 className="mb-4">Novo Pedido (PDV)</h2>

      <Card className="mb-4 shadow-sm">
        <Card.Body>
          <Form.Group>
            <Form.Label className="fw-bold">Selecione o Cliente</Form.Label>
            <Form.Select value={clienteId} onChange={(e) => setClienteId(e.target.value)}>
              <option value="">-- Selecione --</option>
              {clientes.map(c => (
                <option key={c.id} value={c.id}>{c.nome} ({c.email})</option>
              ))}
            </Form.Select>
          </Form.Group>
        </Card.Body>
      </Card>

      <Card className="mb-4 shadow-sm border-primary">
        <Card.Body>
          <Card.Title className="text-primary">Adicionar Itens</Card.Title>
          <Row className="align-items-end">
            <Col md={5}>
              <Form.Group className="mb-3 mb-md-0">
                <Form.Label>Produto</Form.Label>
                <Form.Select value={produtoId} onChange={(e) => setProdutoId(e.target.value)}>
                  <option value="">-- Selecione o Produto --</option>
                  {produtos.map(p => (
                    <option key={p.id} value={p.id}>{p.descricao} - Estoque: {p.quantidade} - R${p.valor.toFixed(2)}</option>
                  ))}
                </Form.Select>
              </Form.Group>
            </Col>
            <Col md={2}>
              <Form.Group className="mb-3 mb-md-0">
                <Form.Label>Qtd</Form.Label>
                <Form.Control type="number" min="1" value={quantidade} onChange={(e) => setQuantidade(e.target.value)} />
              </Form.Group>
            </Col>
            <Col md={2}>
              <Form.Group className="mb-3 mb-md-0">
                <Form.Label>Desconto (R$)</Form.Label>
                <Form.Control type="number" min="0" step="0.01" value={desconto} onChange={(e) => setDesconto(e.target.value)} />
              </Form.Group>
            </Col>
            <Col md={3}>
              <Button variant="outline-primary" className="w-100" onClick={adicionarAoCarrinho}>
                + Adicionar Item
              </Button>
            </Col>
          </Row>
        </Card.Body>
      </Card>

      <Card className="shadow-sm">
        <Card.Body>
          <Card.Title>Carrinho de Compras</Card.Title>
          <Table striped bordered hover responsive className="mt-3">
            <thead className="table-dark">
              <tr>
                <th>Produto</th>
                <th>Valor Unit.</th>
                <th>Qtd</th>
                <th>Desconto</th>
                <th>Subtotal</th>
                <th>Ação</th>
              </tr>
            </thead>
            <tbody>
              {carrinho.map((item, index) => {
                const subtotal = (item.valorUnitario * item.quantidade) - item.desconto;
                return (
                  <tr key={index}>
                    <td>{item.descricao}</td>
                    <td>R$ {item.valorUnitario.toFixed(2)}</td>
                    <td>{item.quantidade}</td>
                    <td>R$ {item.desconto.toFixed(2)}</td>
                    <td className="fw-bold">R$ {subtotal.toFixed(2)}</td>
                    <td>
                      <Button variant="danger" size="sm" onClick={() => removerDoCarrinho(index)}>Remover</Button>
                    </td>
                  </tr>
                );
              })}
              {carrinho.length === 0 && (
                <tr><td colSpan="6" className="text-center">Nenhum item adicionado ao pedido.</td></tr>
              )}
            </tbody>
          </Table>
          
          <div className="d-flex justify-content-end mt-3">
            <Button variant="success" size="lg" onClick={finalizarPedido} disabled={carrinho.length === 0}>
              Finalizar Pedido
            </Button>
          </div>
        </Card.Body>
      </Card>
    </div>
  );
}

export default TelaPedidos;