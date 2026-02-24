import { useState, useEffect } from 'react';
import { Table, Button, Form, Card, Row, Col, Alert } from 'react-bootstrap';
import api from '../api/api';
import Swal from 'sweetalert2';

function TelaClientes() {
  const [clientes, setClientes] = useState([]);
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [termoBusca, setTermoBusca] = useState('');

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
      
      Swal.fire({
        icon: 'success',
        title: 'Show!',
        text: 'Cliente cadastrado com sucesso!',
        showConfirmButton: false,
        timer: 1500
      });

      setNome('');
      setEmail('');
      carregarClientes();
    } catch (error) {
      console.error("Erro ao salvar:", error);
        Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Erro ao cadastrar o cliente.',
      });
    }
  };

  const clientesFiltrados = clientes.filter(cliente => {
    if (termoBusca === '') return true;

    const termo = termoBusca.toLowerCase();
    const matchNome = cliente.nome.toLowerCase().includes(termo);
    const matchId = cliente.id.toString() === termo;
    
    return matchNome || matchId;
  })

  return (
    <div>
      <h2 className="mb-4">Gestão de Clientes</h2>

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
                    onChange={(e) => setNome(e.target.value)}
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
                <th>Data de cadastro</th>
              </tr>
            </thead>
            <tbody>
              {clientes.map((cliente) => (
                <tr key={cliente.id}>
                  <td>{cliente.id}</td>
                  <td>{cliente.nome}</td>
                  <td>{cliente.email}</td>
                  <td>{new Date(cliente.dataCadastro).toLocaleString('pt-BR')}</td>
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