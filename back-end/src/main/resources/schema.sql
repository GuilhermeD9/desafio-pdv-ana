CREATE TABLE IF NOT EXISTS cliente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS produto (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    quantidade INTEGER NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pedido (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER NOT NULL,
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10,2),
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE IF NOT EXISTS item_pedido (
    id SERIAL PRIMARY KEY,
    pedido_id INTEGER NOT NULL,
    produto_id INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    desconto DECIMAL(10, 2) DEFAULT 0,
    CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produto(id)
);

INSERT INTO cliente (nome, email) VALUES
('Ana Souza', 'ana.souza@email.com'),
('Carlos Silva', 'carlos.silva@email.com'),
('Beatriz Costa', 'beatriz.costa@email.com');

INSERT INTO produto (descricao, valor, quantidade) VALUES
('Notebook Dell Inspiron', 4500.00, 10),
('Mouse Sem Fio Logitech', 150.00, 50),
('Monitor LG 29 Ultrawide', 1200.00, 15);

INSERT INTO pedido (cliente_id, valor_total) VALUES
(1, 4650.00),
(2, 1200.00);

INSERT INTO item_pedido (pedido_id, produto_id, quantidade, valor_unitario, desconto) VALUES
(1, 1, 1, 4500.00, 0.00),
(1, 2, 1, 150.00, 0.00);

INSERT INTO item_pedido (pedido_id, produto_id, quantidade, valor_unitario, desconto) VALUES
(2, 3, 1, 1200.00, 0.00)