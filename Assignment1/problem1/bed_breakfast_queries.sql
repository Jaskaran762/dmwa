CREATE SCHEMA IF NOT EXISTS BedBreakfast;
USE BedBreakfast;

---------- Hotel Table-------------
CREATE Table Hotel  (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  phone INT(10) NOT NULL,
  email VARCHAR(45) NOT NULL,
  streetname VARCHAR(45) NOT NULL,
  city VARCHAR(45) NOT NULL,
  country VARCHAR(45) NOT NULL,
  zip VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (id));


-------------- Staff Table----------
  CREATE TABLE Staff  (
  id INT NOT NULL,
  designation VARCHAR(45),
  email VARCHAR(45),
  role VARCHAR(45),
  phone INT(10) ,
  manager_id INT,
  Hotel_id INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (id),
  UNIQUE(Hotel_id),
  CONSTRAINT fk_Staff_Hotel1
    FOREIGN KEY (Hotel_id)
    REFERENCES Hotel(id)
  );

  ------------ Room-----------------
  CREATE TABLE Room (
    id INT NOT NULL,
    room_number VARCHAR(45) NOT NULL,
    availabilIty BOOL NOT NULL DEFAULT TRUE,
    room_type VARCHAR(45) NOT NULL,
    description VARCHAR(200),
    price DECIMAL NOT NULL,
    Hotel_id INT NOT NULL,
    Staff_id INT NOT NULL,
    PRIMARY KEY (id, Hotel_id),
    UNIQUE(id), UNIQUE(room_number),
    UNIQUE(Hotel_id),
    UNIQUE(Staff_id),
    CONSTRAINT fk_Room_Hotel
        FOREIGN KEY (Hotel_id)
        REFERENCES Hotel(id),
    CONSTRAINT fk_Room_Staff1
        FOREIGN KEY (Staff_id)
        REFERENCES Staff(id)
  );


------------ Amenities--------------
CREATE TABLE Amenities (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (id));


  ---------- Meal------------------
  CREATE TABLE Meal (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  price DECIMAL NOT NULL,
  Staff_id INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (id), UNIQUE(Staff_id),
  CONSTRAINT fk_Meal_Staff1
    FOREIGN KEY (Staff_id)
    REFERENCES Staff(id));

------------ Customer---------------
CREATE TABLE Customer (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  phone INT(10) NOT NULL,
  email VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (id));

------------ Payment---------------
CREATE TABLE Payment (
  id INT NOT NULL,
  method VARCHAR(45) NOT NULL,
  price DECIMAL NOT NULL,
  status VARCHAR(45) ,
  Customer_id INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (id),
  UNIQUE(Customer_id),
  CONSTRAINT fk_Payment_Customer1
    FOREIGN KEY (Customer_id)
    REFERENCES Customer(id));

------------ Booking---------------
CREATE TABLE Booking(
  id INT NOT NULL,
  check_in_date DATE NOT NULL,
  check_out_date DATE NOT NULL,
  no_of_guests INT NOT NULL,
  status VARCHAR(45) NOT NULL,
  Customer_id INT NOT NULL,
  Payment_id INT NOT NULL,
  Hotel_id INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (id), UNIQUE (Customer_id),
  UNIQUE (Payment_id), UNIQUE (Hotel_id),
  CONSTRAINT fk_Booking_Customer1
    FOREIGN KEY (Customer_id)
    REFERENCES Customer (id),
  CONSTRAINT fk_Booking_Payment1
    FOREIGN KEY (Payment_id)
    REFERENCES Payment (id),
  CONSTRAINT fk_Booking_Hotel1
    FOREIGN KEY (Hotel_id)
    REFERENCES Hotel (id));

 ------------ Feedback------------
CREATE TABLE Feedback (
  id INT NOT NULL,
  rating INT(1) NOT NULL,
  description VARCHAR(45),
  Customer_id INT NOT NULL,
  Hotel_id INT NOT NULL,
  PRIMARY KEY (id, Customer_id, Hotel_id),
  CONSTRAINT fk_Feedback_Customer1
    FOREIGN KEY (Customer_id)
    REFERENCES Customer (id),
  CONSTRAINT fk_Feedback_Hotel1
    FOREIGN KEY (Hotel_id)
    REFERENCES Hotel (id));

------------- Hotel_has_amenities------
CREATE TABLE Hotel_has_Amenities (
  Hotel_id INT NOT NULL,
  Amenity_id INT NOT NULL,
  PRIMARY KEY (Hotel_id, Amenity_id),
  CONSTRAINT fk_Hotel_has_Amenity_Hotel1
    FOREIGN KEY (Hotel_id)
    REFERENCES Hotel (id),
  CONSTRAINT fk_Hotel_has_Amenity_Amenity1
    FOREIGN KEY (Amenity_id)
    REFERENCES Amenities(id));


-------------- Room_has_Amenities--------
CREATE TABLE Room_has_Amenities (
  Room_id INT NOT NULL,
  Room_Hotel_id INT NOT NULL,
  Amenity_id INT NOT NULL,
  PRIMARY KEY (Room_id, Room_Hotel_id, Amenity_id),
  CONSTRAINT fk_Room_has_Amenity_Room1
    FOREIGN KEY (Room_id , Room_Hotel_id)
    REFERENCES Room (id, Hotel_id),
  CONSTRAINT fk_Room_has_Amenity_Amenity1
    FOREIGN KEY (Amenity_id)
    REFERENCES Amenities (id));


------------ Booking_has_Meal--------------
CREATE TABLE Booking_has_Meal (
  Booking_id INT NOT NULL,
  Meal_id INT NOT NULL,
  PRIMARY KEY (Booking_id, Meal_id),
  CONSTRAINT fk_Booking_has_Meal_Booking1
    FOREIGN KEY (Booking_id)
    REFERENCES Booking (id),
  CONSTRAINT fk_Booking_has_Meal_Meal1
    FOREIGN KEY (Meal_id)
    REFERENCES Meal (id));

---------------- Coupons-----------------------------
CREATE TABLE Coupons (
  id INT NOT NULL,
  description VARCHAR(45) NOT NULL,
  validity DATE NOT NULL,
  type VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (id),
  UNIQUE (type));


----------- Booking_has_Coupons---------------------
CREATE TABLE Booking_has_Coupons (
  Booking_id INT NOT NULL,
  Coupons_id INT NOT NULL,
  PRIMARY KEY (Booking_id, Coupons_id),
  CONSTRAINT fk_Booking_has_Coupons_Booking1
    FOREIGN KEY (Booking_id)
    REFERENCES Booking (id),
  CONSTRAINT fk_Booking_has_Coupons_Coupons1
    FOREIGN KEY (Coupons_id)
    REFERENCES Coupons (id));


------------ Booking_has_Room-----------------------
CREATE TABLE Booking_has_Room (
  Booking_id INT NOT NULL,
  Room_id INT NOT NULL,
  Room_Hotel_id INT NOT NULL,
  PRIMARY KEY (Booking_id, Room_id, Room_Hotel_id),
  CONSTRAINT fk_Booking_has_Room_Booking1
    FOREIGN KEY (Booking_id)
    REFERENCES Booking (id),
  CONSTRAINT fk_Booking_has_Room_Room1
    FOREIGN KEY (Room_id , Room_Hotel_id)
    REFERENCES Room (id, Hotel_id));