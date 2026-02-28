import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Container, Card, Row, Col, Button } from 'react-bootstrap';
import NavBar from './components/NavBar';
import TelaClientes from './pages/TelaClientes';
import TelaProdutos from './pages/TelaProdutos';
import TelaPedidos from './pages/TelaPedidos';
import TelaRelatorios from './pages/TelaRelatorios';

const Home = () => { 
  return (
    <div className="text-center py-5">
      <h1 className="mb-4 fw-bold text-primary">Sistema de Gestão</h1>
      <p className="lead mb-5 text-muted">Gerencie clientes, produtos, pedidos e relatórios em um só lugar</p>
      
      <Row className="g-4 justify-content-center">
        <Col md={6} lg={3}>
          <Card className="h-100 shadow-sm hover-card">
            <Card.Body className="d-flex flex-column align-items-center text-center p-4">
              <div className="mb-3">
                <i className="bi bi-people-fill display-4 text-primary"></i>
              </div>
              <Card.Title className="fw-bold">Gestão de Clientes</Card.Title>
              <Card.Text className="text-muted mb-4">
                Cadastre e gerencie os clientes do seu negócio
              </Card.Text>
              <Button variant="primary" href="/clientes" className="mt-auto">
                Acessar Clientes
              </Button>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={6} lg={3}>
          <Card className="h-100 shadow-sm hover-card">
            <Card.Body className="d-flex flex-column align-items-center text-center p-4">
              <div className="mb-3">
                <i className="bi bi-box-seam-fill display-4 text-success"></i>
              </div>
              <Card.Title className="fw-bold">Gestão de Produtos</Card.Title>
              <Card.Text className="text-muted mb-4">
                Controle seu estoque e produtos disponíveis
              </Card.Text>
              <Button variant="success" href="/produtos" className="mt-auto">
                Acessar Produtos
              </Button>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={6} lg={3}>
          <Card className="h-100 shadow-sm hover-card">
            <Card.Body className="d-flex flex-column align-items-center text-center p-4">
              <div className="mb-3">
                <i className="bi bi-cart-fill display-4 text-warning"></i>
              </div>
              <Card.Title className="fw-bold">Gestão de Pedidos</Card.Title>
              <Card.Text className="text-muted mb-4">
                Registre e acompanhe todos os pedidos
              </Card.Text>
              <Button variant="warning" href="/pedidos" className="mt-auto">
                Acessar Pedidos
              </Button>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={6} lg={3}>
          <Card className="h-100 shadow-sm hover-card">
            <Card.Body className="d-flex flex-column align-items-center text-center p-4">
              <div className="mb-3">
                <i className="bi bi-graph-up display-4 text-info"></i>
              </div>
              <Card.Title className="fw-bold">Central de Relatórios</Card.Title>
              <Card.Text className="text-muted mb-4">
                Visualize estatísticas e relatórios detalhados
              </Card.Text>
              <Button variant="info" href="/relatorios" className="mt-auto">
                Acessar Relatórios
              </Button>
            </Card.Body>
          </Card>
        </Col>
      </Row>
      
      <style>{`
        .hover-card {
          transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
        }
        .hover-card:hover {
          transform: translateY(-5px);
          box-shadow: 0 8px 25px rgba(0,0,0,0.15) !important;
        }
      `}</style>
    </div>
  )
}

function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <Container>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/clientes" element={<TelaClientes />} />
          <Route path="/produtos" element={<TelaProdutos />} />
          <Route path="/pedidos" element={<TelaPedidos />} />
          <Route path="/relatorios" element={<TelaRelatorios />} />
        </Routes>
      </Container>
    </BrowserRouter>
  );
}

export default App