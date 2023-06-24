SET PROFILING = 1;

CREATE SCHEMA IF NOT EXISTS Order_Management;

USE Order_Management;

--------- User table----------------
CREATE TABLE User (
  id INT NOT NULL,
  name VARCHAR(255),
  email VARCHAR(255),
  phone VARCHAR(20),
  address VARCHAR(255),
  PRIMARY KEY(id),
  UNIQUE(id)
);

------------- Order_info table------------
CREATE TABLE Order_info (
  order_id INT NOT NULL,
  user_id INT,
  item_name VARCHAR(255) NOT NULL,
  quantity INT,
  order_date DATE,
  PRIMARY KEY(order_id),
  UNIQUE(order_id),
  FOREIGN KEY (user_id) REFERENCES User(id)
);

INSERT INTO User (id, name, email, phone, address) values (100, 'jas', 'jas@gmai.com', 4566777, 'Lakeside');

SHOW PROFILES;

SELECT * FROM Order_info;