import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import NavBar from './components/NavBar';
import TelaClientes from './pages/TelaClientes';
import TelaProdutos from './pages/TelaProdutos';
import TelaPedidos from './pages/TelaPedidos';

const Home = () => <h2>Bem-vindo ao Sistema de Gestão</h2>;

function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <Container>
        {}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/clientes" element={<TelaClientes />} />
          <Route path="/produtos" element={<TelaProdutos />} />
          <Route path="/pedidos" element={<TelaPedidos />} />
        </Routes>
      </Container>
    </BrowserRouter>
  );
}

export default App