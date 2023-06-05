CREATE SCHEMA IF NOT EXISTS ParkManager;
Use ParkManager;

----- Park table----------------
CREATE TABLE Park (
parkId INT NOT NULL,
parkType VARCHAR(50),
street VARCHAR(50) NOT NULL,
city VARCHAR(50) NOT NULL,
province VARCHAR(50) NOT NULL,
zip VARCHAR(50) NOT NULL,
phone INT(10) NOT NULL,
openingTime DATETIME NOT NULL,
closingTime DATETIME NOT NULL,
PRIMARY KEY(parkId),
UNIQUE(parkId)
); 

----- VISITOR table----------------
CREATE TABLE Visitor (
visitorId INT NOT NULL,
name VARCHAR(50) NOT NULL,
phone INT(10) NOT NULL,
age TINYINT NOT NULL,
gender VARCHAR(15) NOT NULL,
PRIMARY KEY(visitorId),
UNIQUE(visitorId)
); 

----- VENDOR table----------------
CREATE TABLE Vendor (
vendorId INT NOT NULL,
type VARCHAR(50) NOT NULL,
phone INT(10) NOT NULL,
contractStartDateTime DATETIME NOT NULL,
contractEndDateTime DATETIME NOT NULL,
PRIMARY KEY(vendorId),
UNIQUE(vendorId)
); 

----- Event table----------------
CREATE TABLE Event (
eventId INT NOT NULL,
name VARCHAR(50) NOT NULL,
description VARCHAR(500),
contractStartDateTime DATETIME NOT NULL,
contractEndDateTime DATETIME NOT NULL,
parkId INT NOT NULL,
PRIMARY KEY(eventId, parkId),
UNIQUE(eventId),
CONSTRAINT fk_Event_Park1
	FOREIGN KEY(parkId)
    REFERENCES Park(parkId)
); 

----- Site table----------------
CREATE TABLE Site (
siteId INT NOT NULL,
area INT NOT NULL,
capacity INT NOT NULL,
description VARCHAR(500),
parkId INT NOT NULL,
PRIMARY KEY(siteId, parkId),
UNIQUE(siteId),
CONSTRAINT fk_Site_Park1
	FOREIGN KEY(parkId)
    REFERENCES Park(parkId)
); 

----- Staff table----------------
CREATE TABLE Staff (
staffId INT NOT NULL,
name VARCHAR(50) NOT NULL,
position VARCHAR(50) NOT NULL,
role VARCHAR(50) NOT NULL,
phone INT(10) NOT NULL,
parkId INT NOT NULL,
PRIMARY KEY(staffId),
UNIQUE(staffId),
CONSTRAINT fk_Staff_Park1
	FOREIGN KEY(parkId)
    REFERENCES Park(parkId)
); 

----- Alert table----------------
CREATE TABLE Alert (
alertId INT NOT NULL,
name VARCHAR(50) NOT NULL,
description VARCHAR(500),
eventId INT NOT NULL,
parkId INT NOT NULL,
PRIMARY KEY(alertId, parkId),
UNIQUE(alertId),
CONSTRAINT fk_Alert_Park1
	FOREIGN KEY(parkId)
    REFERENCES Park(parkId),
CONSTRAINT fk_Alert_Event1
	FOREIGN KEY(eventId)
    REFERENCES Event(eventId)
); 

----- Rule table----------------
CREATE TABLE Rule (
ruleId INT NOT NULL,
name VARCHAR(50) NOT NULL,
description VARCHAR(500),
eventId INT NOT NULL,
parkId INT NOT NULL,
PRIMARY KEY(ruleId, parkId),
UNIQUE(ruleId),
CONSTRAINT fk_Rule_Park1
	FOREIGN KEY(parkId)
    REFERENCES Park(parkId),
CONSTRAINT fk_Rule_Event1
	FOREIGN KEY(eventId)
    REFERENCES Event(eventId)
); 

----- Facility table----------------
CREATE TABLE Facility (
facilityId INT NOT NULL,
name VARCHAR(50) NOT NULL,
type VARCHAR(50) NOT NULL,
capacity INT NOT NULL,
parkId INT NOT NULL,
siteId INT NOT NULL,
staffId INT NOT NULL,
PRIMARY KEY(facilityId),
UNIQUE(facilityId),
CONSTRAINT fk_Facility_Park1
	FOREIGN KEY(parkId)
    REFERENCES Park(parkId),
CONSTRAINT fk_Facility_Site1
	FOREIGN KEY(siteId)
    REFERENCES Site(siteid),
CONSTRAINT fk_Facility_Staff1
	FOREIGN KEY(staffId)
    REFERENCES Staff(staffId)
);

----- Equipment table----------------
CREATE TABLE Equipment (
equipmentId INT NOT NULL,
name VARCHAR(50) NOT NULL,
type VARCHAR(50) NOT NULL,
quantity INT NOT NULL,
purchaseDate DATETIME NOT NULL,
warrantyInfo VARCHAR(100) NOT NULL,
replacementCost DECIMAL(10) NOT NULL,
facilityId INT NOT NULL,
vendorId INT NOT NULL,
PRIMARY KEY(equipmentId),
UNIQUE(equipmentid),
CONSTRAINT fk_Equipment_Facility1
	FOREIGN KEY(facilityId)
    REFERENCES Facility(facilityId),
CONSTRAINT fk_Equipment_Vendor1
	FOREIGN KEY(vendorId)
    REFERENCES Vendor(vendorId)
);

----- Payment table----------------
CREATE TABLE Payment (
paymentId INT NOT NULL,
method VARCHAR(50) NOT NULL,
price DECIMAL(10) NOT NULL,
paymentStatus VARCHAR(50) NOT NULL,
dateTime DATETIME NOT NULL,
visitorId INT NOT NULL,
PRIMARY KEY(paymentId),
UNIQUE(paymentId),
CONSTRAINT fk_Payment_Visitor1
	FOREIGN KEY(visitorId)
    REFERENCES Visitor(visitorId)
);

----- Reservation table----------------
CREATE TABLE Reservation (
reservationId INT NOT NULL,
checkInDate DATETIME NOT NULL,
checkOutDate DATETIME NOT NULL,
noOfVisitor INT NOT NULL,
status VARCHAR(20) NOT NULL,
parkId INT NOT NULL,
visitorId INT NOT NULL,
siteId INT NOT NULL,
paymentId INT NOT NULL,
equipmentId INT NOT NULL,
eventId INT NOT NULL,
PRIMARY KEY(reservationId),
UNIQUE(reservationId),
CONSTRAINT fk_Reservation_Park1
	FOREIGN KEY(parkId)
    REFERENCES Park(ParkId),
CONSTRAINT fk_Reservation_Visitor1
	FOREIGN KEY(visitorId)
    REFERENCES Visitor(visitorId),
CONSTRAINT fk_Reservation_Site1
	FOREIGN KEY(siteId)
    REFERENCES Site(siteId),
CONSTRAINT fk_Reservation_Payment1
	FOREIGN KEY(paymentId)
    REFERENCES Payment(paymentId),
CONSTRAINT fk_Reservation_Equipment1
	FOREIGN KEY(equipmentId)
    REFERENCES Equipment(equipmentId),
CONSTRAINT fk_Reservation_Event1
	FOREIGN KEY(eventId)
    REFERENCES Event(eventId)
);

----- Reservation_has_facility table----------------
CREATE TABLE Reservation_has_facility (
reservationId INT NOT NULL,
facilityId INT NOT NULL,
PRIMARY KEY(reservationId, facilityid),
CONSTRAINT fk_Reservation_has_facility1
	FOREIGN KEY(reservationId)
    REFERENCES Reservation(reservationId),
CONSTRAINT fk_Reservation_has_facility2
	FOREIGN KEY(facilityId)
    REFERENCES Facility(facilityId)
);

----- Park_has_vendor table----------------
CREATE TABLE Park_has_vendor (
parkId INT NOT NULL,
vendorId INT NOT NULL,
PRIMARY KEY(parkId, vendorId),
CONSTRAINT fk_Park_has_vendor1
	FOREIGN KEY(parkId)
    REFERENCES Park(parkId),
CONSTRAINT fk_Park_has_vendor2
	FOREIGN KEY(vendorId)
    REFERENCES Vendor(vendorId)
);

