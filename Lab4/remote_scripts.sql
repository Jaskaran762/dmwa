SET PROFILING = 1;

CREATE SCHEMA IF NOT EXISTS Inventory_Management;

USE Inventory_Management;

-------- Inventory table----------
CREATE TABLE Inventory (
  id INT NOT NULL,
  item_name VARCHAR(255) NOT NULL,
  available_quantity INT,
  PRIMARY KEY(id),
  UNIQUE(id)
);

INSERT INTO Inventory (id, item_name, available_quantity) values (1, 'pen', 300);

SHOW PROFILES;