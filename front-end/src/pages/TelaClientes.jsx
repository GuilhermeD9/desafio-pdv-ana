import { useState, useEffect } from 'react';
import { Table, Button, Form, Card, Row, Col, Alert, InputGroup } from 'react-bootstrap';
import api from '../api/api';
import Swal from 'sweetalert2';

function TelaClientes() {
  const [clientes, setClientes] = useState([]);
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [termoBusca, setTermoBusca] = useState('');
  const [termoAplicado, setTermoAplicado] = useState('');
  const [tipoBusca, setTipoBusca] = useState('id');

  useEffect(() => {
    carregarClientes();
  }, []);

  const carregarClientes = async () => {
    try {
      const resposta = await api.get('/clientes');
      setClientes(resposta.data); 
    } catch (error) {
      console.error("Erro ao buscar clientes:", error);
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Erro ao carregar a lista de clientes.',
      });
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

  const clientesFiltrados = clientes.filter(cliente => {
    if (termoAplicado === '') return true;

    const termo = termoAplicado.toLowerCase();

    if (tipoBusca === 'id') {
      return cliente.id.toString() === termo;
    } 
    
    if (tipoBusca === 'nome') {
      return cliente.nome.toLowerCase().includes(termo);
    }
    
    return false;
  });

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
          <Row className="mb-3 align-items-end">
            <Col md={5}>
              <Card.Title className="mb-0">Lista de Clientes</Card.Title>
            </Col>
            
            <Col md={7}>
              <div className="mb-2 d-flex gap-4">
                <Form.Check 
                  type="radio"
                  id="radio-id"
                  label="Buscar por ID"
                  name="tipoBuscaGroup"
                  value="id"
                  checked={tipoBusca === 'id'}
                  onChange={(e) => setTipoBusca(e.target.value)}
                />
                <Form.Check 
                  type="radio"
                  id="radio-nome"
                  label="Buscar por Nome"
                  name="tipoBuscaGroup"
                  value="nome"
                  checked={tipoBusca === 'nome'}
                  onChange={(e) => setTipoBusca(e.target.value)}
                />
              </div>

              <InputGroup>
                <Form.Control
                  placeholder={tipoBusca === 'id' ? "Digite o número do ID..." : "Digite o nome do cliente..."}
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
              </InputGroup>
            </Col>
          </Row>

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
              {clientesFiltrados.map((cliente) => (
                <tr key={cliente.id}>
                  <td>{cliente.id}</td>
                  <td>{cliente.nome}</td>
                  <td>{cliente.email}</td>
                  <td>{new Date(cliente.dataCadastro).toLocaleString('pt-BR')}</td>
                </tr>
              ))}
              {clientesFiltrados.length === 0 && (
                <tr>
                  <td colSpan="4" className="text-center">
                    {termoBusca ? 'Nenhum cliente encontrado na busca.' : 'Nenhum cliente cadastrado.'}
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

export default TelaClientes;