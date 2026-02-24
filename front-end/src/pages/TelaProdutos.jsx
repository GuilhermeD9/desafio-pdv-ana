import { useState, useEffect } from 'react';
import { Table, Button, Form, Card, Row, Col, Alert } from 'react-bootstrap';
import api from '../api/api';

function TelaProdutos() {
  const [produtos, setProdutos] = useState([]);
  const [descricao, setDescricao] = useState('');
  const [valor, setValor] = useState('');
  const [quantidade, setQuantidade] = useState('');
  const [mensagem, setMensagem] = useState('');

  useEffect(() => {
    carregarProdutos();
  }, []);

  const carregarProdutos = async () => {
    try {
      const resposta = await api.get('/produtos');
      setProdutos(resposta.data);
    } catch (error) {
      console.error("Erro ao buscar produtos:", error);
      setMensagem("Erro ao carregar a lista de produtos.");
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
      setMensagem("Produto cadastrado com sucesso!");
      setDescricao('');
      setValor('');
      setQuantidade('');
      carregarProdutos();
    } catch (error) {
      console.error("Erro ao salvar:", error);
      setMensagem("Erro ao cadastrar o produto.");
    }
  };

  return (
    <div>
      <h2 className="mb-4">Gestão de Produtos</h2>

      {mensagem && <Alert variant={mensagem.includes('sucesso') ? 'success' : 'danger'}>{mensagem}</Alert>}

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
              {produtos.map((produto) => (
                <tr key={produto.id}>
                  <td>{produto.id}</td>
                  <td>{produto.descricao}</td>
                  <td>{produto.valor.toFixed(2)}</td>
                  <td>{produto.quantidade}</td>
                </tr>
              ))}
              {produtos.length === 0 && (
                <tr>
                  <td colSpan="4" className="text-center">Nenhum produto cadastrado.</td>
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