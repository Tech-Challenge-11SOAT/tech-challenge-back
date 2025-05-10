-- Inserção de produtos padrão (5 por categoria)

-- Lanches (id_categoria = 1)
INSERT INTO produto (nome, descricao, preco, id_categoria) VALUES
('Cheeseburger', 'Hambúrguer com queijo, alface e tomate.', 18.90, 1),
('X-Bacon', 'Hambúrguer com queijo, bacon e maionese especial.', 21.50, 1),
('Veggie Burger', 'Hambúrguer vegetal com salada.', 19.90, 1),
('Chicken Burger', 'Sanduíche de frango crocante.', 20.00, 1),
('Duplo Smash', 'Dois burgers smash com queijo e cebola caramelizada.', 24.00, 1);

-- Acompanhamentos (id_categoria = 2)
INSERT INTO produto (nome, descricao, preco, id_categoria) VALUES
('Batata Frita', 'Batata palito crocante.', 9.90, 2),
('Onion Rings', 'Anéis de cebola empanados.', 10.90, 2),
('Batata Rústica', 'Batata rústica com ervas e alho.', 11.50, 2),
('Nuggets', 'Porção com 6 unidades de nuggets de frango.', 12.00, 2),
('Cheddar Fries', 'Batata com cheddar e bacon.', 14.50, 2);

-- Bebidas (id_categoria = 3)
INSERT INTO produto (nome, descricao, preco, id_categoria) VALUES
('Refrigerante Lata', 'Coca-Cola, Guaraná ou Fanta (350ml).', 6.00, 3),
('Suco Natural', 'Suco natural de laranja ou limão.', 7.00, 3),
('Água Mineral', 'Água sem gás (500ml).', 3.50, 3),
('Chá Gelado', 'Chá preto com limão.', 5.50, 3),
('Refrigerante 600ml', 'Garrafa de 600ml.', 7.50, 3);

-- Sobremesas (id_categoria = 4)
INSERT INTO produto (nome, descricao, preco, id_categoria) VALUES
('Brownie', 'Brownie de chocolate com calda quente.', 8.90, 4),
('Sorvete', '2 bolas de sorvete à escolha.', 7.50, 4),
('Milkshake', 'Milkshake de chocolate, morango ou baunilha.', 10.90, 4),
('Petit Gâteau', 'Bolo quente com recheio cremoso e sorvete.', 12.00, 4),
('Cookie Recheado', 'Cookie gigante com recheio de Nutella.', 9.00, 4);
