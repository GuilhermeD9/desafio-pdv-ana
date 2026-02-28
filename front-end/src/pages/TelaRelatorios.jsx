import { useState, useEffect } from 'react';
import { Card, Button, Form, Row, Col, Table, Tabs, Tab } from 'react-bootstrap';
import Swal from 'sweetalert2';
import api from '../api/api';

function TelaRelatorios() {
  const [clientes, setClientes] = useState([]);
  const [produtos, setProdutos] = useState([]);
  const [pedidos, setPedidos] = useState([]);
  const [totalGasto, setTotalGasto] = useState(null);
  const [modoExibicao, setModoExibicao] = useState('tabela');
  const [pedidoDetalhado, setPedidoDetalhado] = useState(null);
  const [clienteId, setClienteId] = useState('');
  const [produtoId, setProdutoId] = useState('');
  const [dataInicio, setDataInicio] = useState('');
  const [dataFim, setDataFim] = useState('');
  const [pedidoId, setPedidoId] = useState('');

  useEffect(() => {
    async function carregarDados() {
      try {
        const resClientes = await api.get('/clientes');
        const resProdutos = await api.get('/produtos');
        setClientes(resClientes.data);
        setProdutos(resProdutos.data);
      } catch (error) {
      }
    }
    carregarDados();
  }, []);

  const buscarPorCliente = async () => {
    if (!clienteId) return Swal.fire('Atenção', 'Selecione um cliente.', 'warning');
    try {
      const resPedidos = await api.get(`/pedidos/cliente?clienteId=${clienteId}`);
      const resTotal = await api.get(`/pedidos/cliente/valorTotal?clienteId=${clienteId}`);
      setPedidos(resPedidos.data);
      setTotalGasto(resTotal.data);
      setModoExibicao('tabela')
    } catch (error) {
      setPedidos([]);
      setTotalGasto(null);
      Swal.fire('Ops', 'Nenhum pedido encontrado para este cliente.', 'info');
    }
  };

  const buscarPorProduto = async () => {
    if (!produtoId) return Swal.fire('Atenção', 'Selecione um produto.', 'warning');
    try {
      const res = await api.get(`/pedidos/produto?produtoId=${produtoId}`);
      setPedidos(res.data);
      setTotalGasto(null);
      setModoExibicao('tabela')
    } catch (error) {
      setPedidos([]);
      Swal.fire('Ops', 'Nenhum pedido encontrado com este produto.', 'info');
    }
  };

  const buscarPorPeriodo = async () => {
    if (!dataInicio || !dataFim) return Swal.fire('Atenção', 'Preencha as duas datas.', 'warning');
    try {
      const res = await api.get(`/pedidos/buscar/periodo?inicio=${dataInicio}&fim=${dataFim}`);
      setPedidos(res.data);
      setTotalGasto(null);        
      setModoExibicao('tabela')
    } catch (error) {
      setPedidos([]);
      Swal.fire('Ops', 'Nenhum pedido encontrado neste período.', 'info');
    }
  };

  const buscarPorId = async () => {
    if (!pedidoId) return Swal.fire('Atenção', 'Digite o ID do pedido.', 'warning');
    try {
      const res = await api.get(`/pedidos/${pedidoId}`);
      setPedidoDetalhado(res.data); 
      setTotalGasto(null);
      setModoExibicao('detalhe')
    } catch (error) {
      setPedidoDetalhado(null);
      setModoExibicao('tabela')
      Swal.fire('Ops', 'Pedido não encontrado.', 'error');
    }
  };

  return (
    <div>
      <h2 className="mb-4">Central de Relatórios</h2>

      <Card className="shadow-sm mb-4">
        <Card.Body>
          <Tabs defaultActiveKey="cliente" id="abas-relatorios" className="mb-3">
            
            <Tab eventKey="cliente" title="Por Cliente">
              <Row>
                <Col md={8}>
                  <Form.Select value={clienteId} onChange={e => setClienteId(e.target.value)}>
                    <option value="">Selecione o Cliente</option>
                    {clientes.map(c => <option key={c.id} value={c.id}>{c.nome}</option>)}
                  </Form.Select>
                </Col>
                <Col md={4}>
                  <Button variant="primary" className="w-100" onClick={buscarPorCliente}>Buscar</Button>
                </Col>
              </Row>
            </Tab>

            <Tab eventKey="produto" title="Por Produto">
              <Row>
                <Col md={8}>
                  <Form.Select value={produtoId} onChange={e => setProdutoId(e.target.value)}>
                    <option value="">Selecione o Produto</option>
                    {produtos.map(p => <option key={p.id} value={p.id}>{p.descricao}</option>)}
                  </Form.Select>
                </Col>
                <Col md={4}>
                  <Button variant="primary" className="w-100" onClick={buscarPorProduto}>Buscar</Button>
                </Col>
              </Row>
            </Tab>

            <Tab eventKey="periodo" title="Por Período">
              <Row>
                <Col md={4}>
                  <Form.Control type="date" value={dataInicio} onChange={e => setDataInicio(e.target.value)} />
                </Col>
                <Col md={4}>
                  <Form.Control type="date" value={dataFim} onChange={e => setDataFim(e.target.value)} />
                </Col>
                <Col md={4}>
                  <Button variant="primary" className="w-100" onClick={buscarPorPeriodo}>Buscar</Button>
                </Col>
              </Row>
            </Tab>

            <Tab eventKey="id" title="Por ID">
              <Row>
                <Col md={8}>
                  <Form.Control 
                    type="number" 
                    placeholder="Digite o ID do pedido" 
                    value={pedidoId} 
                    onChange={e => setPedidoId(e.target.value)}
                    onKeyDown={(e) => {
                      if (e.key === 'Enter') {
                        e.preventDefault();
                        buscarPorId();
                      }
                    }}
                  />
                </Col>
                <Col md={4}>
                  <Button variant="primary" className="w-100" onClick={buscarPorId}>Buscar</Button>
                </Col>
              </Row>
            </Tab>

          </Tabs>
        </Card.Body>
      </Card>

      <Card className="shadow-sm">
        <Card.Body>
          <Card.Title>Resultados da Busca</Card.Title>
          
          {totalGasto !== null && (
            <div className="alert alert-success mt-3">
              <strong>Total gasto por este cliente em nossa loja: </strong> R$ {totalGasto.toFixed(2)}
            </div>
          )}

          {modoExibicao === 'tabela' && (
            <Table striped bordered hover responsive className="mt-3">
              <thead className="table-dark">
                <tr>
                  <th>ID do Pedido</th>
                  <th>Data</th>
                  <th>ID do Cliente</th>
                  <th className="text-center">Qtd de Itens Diferentes</th>
                  <th>Valor Total (R$)</th>
                </tr>
              </thead>
              <tbody>
                {pedidos.map(pedido => (
                  <tr key={pedido.id}>
                    <td>{pedido.id}</td>
                    <td>{new Date(pedido.dataPedido).toLocaleString('pt-BR')}</td>
                    <td>{pedido.clienteId}</td>
                    <td className="text-center">{pedido.itens ? pedido.itens.length : 0}</td>
                    <td className="fw-bold">R$ {pedido.valorTotal.toFixed(2)}</td>
                  </tr>
                ))}
                {pedidos.length === 0 && (
                  <tr><td colSpan="5" className="text-center">Faça uma busca para ver os resultados.</td></tr>
                )}
              </tbody>
            </Table>
          )}

          {modoExibicao === 'detalhe' && pedidoDetalhado && (
            <div className="mt-4 p-4 border rounded bg-light border-primary">
              <Row className="mb-3 border-bottom pb-2">
                <Col md={6}>
                  <h4 className="text-primary mb-0">Detalhes do Pedido #{pedidoDetalhado.id}</h4>
                </Col>
                <Col md={6} className="text-md-end text-muted mt-2 mt-md-0">
                  Data: {new Date(pedidoDetalhado.dataPedido).toLocaleString('pt-BR')}
                </Col>
              </Row>

              <Row className="mb-4">
                <Col md={12}>
                  <h6 className="fw-bold">Informações do Cliente:</h6>
                  <p className="mb-0">
                    {clientes.find(c => c.id === pedidoDetalhado.clienteId)?.nome || `Cliente ID: ${pedidoDetalhado.clienteId}`}
                  </p>
                </Col>
              </Row>

              <h6 className="fw-bold text-secondary">Itens Comprados</h6>
              <Table size="sm" striped bordered hover responsive className="mt-2 bg-white">
                <thead className="table-secondary">
                  <tr>
                    <th>Produto</th>
                    <th className="text-center">Qtd</th>
                    <th>Valor Unit. (R$)</th>
                    <th>Subtotal (R$)</th>
                  </tr>
                </thead>
                <tbody>
                  {pedidoDetalhado.itens && pedidoDetalhado.itens.length > 0 ? (
                    pedidoDetalhado.itens.map((item, index) => (
                      <tr key={index}>
                        <td>{item.descricao || `Produto ${item.produtoId}`}</td>
                        <td className="text-center">{item.quantidade}</td>
                        <td>{item.valorUnitario.toFixed(2)}</td>
                        <td>{item.subtotal.toFixed(2)}</td>
                      </tr>
                    ))
                  ) : (
                    <tr><td colSpan="4" className="text-center">Nenhum item encontrado neste pedido.</td></tr>
                  )}
                </tbody>
              </Table>
              
              <div className="d-flex justify-content-end mt-4">
                <h4 className="text-success fw-bold border p-2 rounded bg-white shadow-sm">
                  Valor do pedido: R$ {pedidoDetalhado.valorTotal.toFixed(2)}
                </h4>
              </div>
            </div>
          )}
        </Card.Body>
      </Card>
    </div>
  );
}

export default TelaRelatorios;