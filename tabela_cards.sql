CREATE TABLE minhacolecao (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nome VARCHAR(255) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    tipo VARCHAR(255) NOT NULL,
    localImage VARCHAR(255),
    cardGameName VARCHAR(50) NOT NULL,
    possuir BOOLEAN NOT NULL
);

INSERT INTO minhacolecao (nome, valor, tipo, localImage, cardGameName, possuir)
VALUES
    ('blue eyes white dragon', 19.99, 'ultra rare', 'blue-eyes-white-dragon-ldk2-enk01-common.jpg', 'Yu-Gi-Oh!', 1),
    ('Exodia', 21.99, 'ultra rare', 'yugioh_mc1-en001_en.jpg', 'Yu-Gi-Oh!', 1),
    ('magilaçador de elite', 10.99, 'common', '8df955bb9d03eff82a5d282a38bda38b.png', 'Magic', 0),
    ('mew', 199.99, 'ultra rare', '209,99.jpg', 'Pokemon TCG', 1);