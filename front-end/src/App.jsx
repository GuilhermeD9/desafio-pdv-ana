import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import NavBar from './components/NavBar';

const Home = () => <h2>Bem-vindo ao Sistema de Gestão</h2>;
const TelaClientes = () => <h2>Tela de Clientes (Em breve)</h2>;
const TelaProdutos = () => <h2>Tela de Produtos (Em breve)</h2>;
const TelaPedidos = () => <h2>Tela de Pedidos (Em breve)</h2>;

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

export default App;
