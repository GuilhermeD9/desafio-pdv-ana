import { useState, useEffect } from 'react';
import { Table, Button, Form, Card, Row, Col, Alert } from 'react-bootstrap';
import api from '../api/api';

function TelaClientes() {
  const [clientes, setClientes] = useState([]);
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [mensagem, setMensagem] = useState('');

  useEffect(() => {
    carregarClientes();
  }, []);

  const carregarClientes = async () => {
    try {
      const resposta = await api.get('/clientes');
      setClientes(resposta.data); 
    } catch (error) {
      console.error("Erro ao buscar clientes:", error);
      setMensagem("Erro ao carregar a lista de clientes.");
    }
  };

  const salvarCliente = async (e) => {
    e.preventDefault();
    try {
      await api.post('/clientes', { nome: nome, email: email });
      setMensagem("Cliente cadastrado com sucesso!");
      setNome('');
      setEmail('');
      carregarClientes();
    } catch (error) {
      console.error("Erro ao salvar:", error);
      setMensagem("Erro ao cadastrar o cliente.");
    }
  };

  return (
    <div>
      <h2 className="mb-4">Gestão de Clientes</h2>

      {mensagem && <Alert variant={mensagem.includes('sucesso') ? 'success' : 'danger'}>{mensagem}</Alert>}

      <Card className="mb-4 shadow-sm">
        <Card.Body>
          <Card.Title>Novo Cliente</Card.Title>
          <Form onSubmit={salvarCliente}>
            <Row>
              <Col md={5}>
                <Form.Group className="mb-3">
                  <Form.Label>Nome</Form.Label>
                  <Form.Control 
                    type="text" 
                    placeholder="Digite o nome" 
                    value={nome}
                    onChange={(e) => setNome(e.target.value)} // Atualiza a memória enquanto digita
                    required 
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Group className="mb-3">
                  <Form.Label>E-mail</Form.Label>
                  <Form.Control 
                    type="email" 
                    placeholder="Digite o e-mail" 
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
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
                <th>Nome</th>
                <th>E-mail</th>
              </tr>
            </thead>
            <tbody>
              {clientes.map((cliente) => (
                <tr key={cliente.id}>
                  <td>{cliente.id}</td>
                  <td>{cliente.nome}</td>
                  <td>{cliente.email}</td>
                </tr>
              ))}
              {clientes.length === 0 && (
                <tr>
                  <td colSpan="3" className="text-center">Nenhum cliente cadastrado.</td>
                </tr>
              )}
            </tbody>
          </Table>
        </Card.Body>
      </Card>
    </div>
  );
}

export default TelaClientes;