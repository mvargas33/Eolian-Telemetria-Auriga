-- creating database
CREATE DATABASE eolianmysql;

-- using database
use eolianmysql;

-- creating a table
CREATE TABLE customer(
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  address VARCHAR(100) NOT NULL,
  phone VARCHAR(15)
);

-- mostrar todas la tablas
SHOW TABLES;

-- mostrar estructura
describe customer;
