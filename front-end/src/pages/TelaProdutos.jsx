import { useState, useEffect } from 'react';
import { Table, Button, Form, Card, Row, Col } from 'react-bootstrap';
import api from '../api/api';
import Swal from 'sweetalert2';

function TelaProdutos() {
  const [produtos, setProdutos] = useState([]);
  const [descricao, setDescricao] = useState('');
  const [valor, setValor] = useState('');
  const [quantidade, setQuantidade] = useState('');
  const [termoBusca, setTermoBusca] = useState('');
  const [termoAplicado, setTermoAplicado] = useState('');

  useEffect(() => {
    carregarProdutos();
  }, []);

  const carregarProdutos = async () => {
    try {
      const resposta = await api.get('/produtos');
      setProdutos(resposta.data);
    } catch (error) {
      console.error("Erro ao buscar produtos:", error);
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: 'Erro ao carregar a lista de produtos.',
            });
    }
  };

  const salvarProduto = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        descricao: descricao,
        valor: parseFloat(valor),
        quantidade: parseInt(quantidade, 10)
      };

      await api.post('/produtos', payload);
      
        Swal.fire({
            icon: 'success',
            title: 'Show!',
            text: 'Produto cadastrado com sucesso!',
            showConfirmButton: false,
            timer: 1500
        });

      setDescricao('');
      setValor('');
      setQuantidade('');
      carregarProdutos();
    } catch (error) {
      if (error.response && error.response.data) {
        const dadosErro = error.response.data;
        if (dadosErro.erro) {
          Swal.fire({ icon: 'error', title: 'Erro', text: dadosErro.erro });
        } else {
          const mensagensValidacao = Object.values(dadosErro).join('\n');
          Swal.fire({ icon: 'warning', title: 'Dados Inválidos', text: mensagensValidacao });
        }
      } else {
        Swal.fire({ icon: 'error', title: 'Oops...', text: 'Erro ao conectar com o servidor.' });
      }
    }
  };

  const realizarBusca = () => {
    setTermoAplicado(termoBusca);
  };

  const limparBusca = () => {
    setTermoBusca('');
    setTermoAplicado('');
  };

  const produtosFiltrados = produtos.filter(produto => {
    if (termoAplicado === '') return true;

    const termo = termoAplicado.toLowerCase();
    const matchDescricao = produto.descricao.toLowerCase().includes(termo);
    const matchId = produto.id.toString() === termo;
    
    return matchDescricao || matchId;
  });

  return (
    <div>
      <h2 className="mb-4">Gestão de Produtos</h2>

      <Card className="mb-4 shadow-sm">
        <Card.Body>
          <Card.Title>Novo Produto</Card.Title>
          <Form onSubmit={salvarProduto}>
            <Row>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Descrição</Form.Label>
                  <Form.Control 
                    type="text" 
                    placeholder="Ex: Monitor Gamer" 
                    value={descricao}
                    onChange={(e) => setDescricao(e.target.value)}
                    required 
                  />
                </Form.Group>
              </Col>
              <Col md={3}>
                <Form.Group className="mb-3">
                  <Form.Label>Valor (R$)</Form.Label>
                  <Form.Control 
                    type="number" 
                    step="0.01"
                    placeholder="0.00" 
                    value={valor}
                    onChange={(e) => setValor(e.target.value)}
                    required 
                  />
                </Form.Group>
              </Col>
              <Col md={3}>
                <Form.Group className="mb-3">
                  <Form.Label>Quantidade Inicial</Form.Label>
                  <Form.Control 
                    type="number" 
                    placeholder="0" 
                    value={quantidade}
                    onChange={(e) => setQuantidade(e.target.value)}
                    required 
                  />
                </Form.Group>
              </Col>
              <Col md={2} className="d-flex align-items-end">
                <Button variant="primary" type="submit" className="w-100 mb-3">
                  Salvar
                </Button>
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>

      <Card className="shadow-sm">
        <Card.Body>
          <Row className="mb-3 align-items-center">
            <Col md={6}>
              <Card.Title className="mb-0">Lista de Produtos</Card.Title>
            </Col>
            <Col md={6}>
              <div className="d-flex gap-2">
                <Form.Control
                  placeholder="Buscar por Descrição ou ID..."
                  value={termoBusca}
                  onChange={(e) => setTermoBusca(e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' ? realizarBusca() : null}
                />
                <Button variant="primary" onClick={realizarBusca}>
                  Buscar
                </Button>
                <Button variant="outline-secondary" onClick={limparBusca}>
                  Limpar
                </Button>
              </div>
            </Col>
          </Row>

          <Table striped bordered hover responsive>
            <thead className="table-dark">
              <tr>
                <th>ID</th>
                <th>Descrição</th>
                <th>Valor (R$)</th>
                <th>Estoque</th>
              </tr>
            </thead>
            <tbody>
              {produtosFiltrados.map((produto) => (
                <tr key={produto.id}>
                  <td>{produto.id}</td>
                  <td>{produto.descricao}</td>
                  <td>{produto.valor.toFixed(2)}</td>
                  <td>{produto.quantidade}</td>
                </tr>
              ))}
              {produtosFiltrados.length === 0 && (
                <tr>
                  <td colSpan="4" className="text-center">
                    {termoAplicado ? 'Nenhum produto encontrado na busca.' : 'Nenhum produto cadastrado.'}
                  </td>
                </tr>
              )}
            </tbody>
          </Table>
        </Card.Body>
      </Card>
    </div>
  );
}

export default TelaProdutos;