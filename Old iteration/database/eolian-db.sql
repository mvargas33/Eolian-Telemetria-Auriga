-- creating database
CREATE DATABASE eolianmysql;

-- using database
use eolianmysql;

-- creating a table
CREATE TABLE datosgenerales(
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  potencia VARCHAR(5) NOT NULL,
  potenciasolar VARCHAR(5) NOT NULL,
  velocidad VARCHAR(3) NOT NULL,
  cargabateria VARCHAR(3) NOT NULL,
  tempbateria VARCHAR(3) NOT NULL,
  tempmotord VARCHAR(3) NOT NULL,
  tempmotori VARCHAR(3) NOT NULL
);

-- mostrar todas la tablas
SHOW TABLES;

-- mostrar estructura
describe datosgenerales;
