-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Restaurant
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Restaurant
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Restaurant` DEFAULT CHARACTER SET utf8 ;
USE `Restaurant` ;

-- -----------------------------------------------------
-- Table `Restaurant`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Customer` (
  `customer_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `phone_number` INT(10) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`customer_id`),
  UNIQUE INDEX `customer_id_UNIQUE` (`customer_id` ASC) VISIBLE,
  UNIQUE INDEX `Email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `phone_number_UNIQUE` (`phone_number` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurant`.`Order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Order` (
  `order_id` INT NOT NULL,
  `time` DATETIME NOT NULL DEFAULT now(),
  `quantity` INT NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `Menu_menu_id` INT NOT NULL,
  PRIMARY KEY (`order_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurant`.`Food_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Food_items` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `price` DECIMAL NOT NULL,
  `Order_order_id` INT NOT NULL,
  UNIQUE INDEX `item_id_UNIQUE` (`id` ASC) VISIBLE,
  PRIMARY KEY (`id`, `Order_order_id`),
  INDEX `fk_Food_items_Order1_idx` (`Order_order_id` ASC) VISIBLE,
  CONSTRAINT `fk_Food_items_Order1`
    FOREIGN KEY (`Order_order_id`)
    REFERENCES `Restaurant`.`Order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurant`.`Reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Reservation` (
  `code` INT NOT NULL,
  `time` DATETIME NOT NULL,
  `no_of_customers` INT NOT NULL,
  `Customer_customer_id` INT NOT NULL,
  `Reservation_Customer_customer_id` INT NOT NULL,
  PRIMARY KEY (`code`),
  UNIQUE INDEX `id_UNIQUE` (`code` ASC) VISIBLE,
  INDEX `fk_Reservation_Customer1_idx` (`Customer_customer_id` ASC) VISIBLE,
  CONSTRAINT `fk_Reservation_Customer`
    FOREIGN KEY (`Customer_customer_id`)
    REFERENCES `Restaurant`.`Customer` (`customer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurant`.`Restaurant`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Restaurant` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `noOfEmployees` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idRestaurant_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurant`.`Employee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Employee` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `phone_number` INT(10) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  `Restaurant_id` INT NOT NULL,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  INDEX `fk_Employee_Restaurant1_idx` (`Restaurant_id` ASC) VISIBLE,
  CONSTRAINT `fk_Employee_Restaurant1`
    FOREIGN KEY (`Restaurant_id`)
    REFERENCES `Restaurant`.`Restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurant`.`Dining_table`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Dining_table` (
  `id` INT NOT NULL,
  `capacity` VARCHAR(45) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `Employee_id` INT NOT NULL,
  `Restaurant_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_Dining_table_Employee1_idx` (`Employee_id` ASC) VISIBLE,
  INDEX `fk_Dining_table_Restaurant1_idx` (`Restaurant_id` ASC) VISIBLE,
  CONSTRAINT `fk_Dining_table_Employee1`
    FOREIGN KEY (`Employee_id`)
    REFERENCES `Restaurant`.`Employee` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Dining_table_Restaurant1`
    FOREIGN KEY (`Restaurant_id`)
    REFERENCES `Restaurant`.`Restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurant`.`Reservation_has_Dining_table`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Reservation_has_Dining_table` (
  `Reservation_code` INT NOT NULL,
  `Reservation_Customer_customer_id` INT NOT NULL,
  `Dining_table_id` INT NOT NULL,
  PRIMARY KEY (`Reservation_code`, `Reservation_Customer_customer_id`, `Dining_table_id`),
  INDEX `fk_Reservation_has_Dining_table_Dining_table1_idx` (`Dining_table_id` ASC) VISIBLE,
  INDEX `fk_Reservation_has_Dining_table_Reservation1_idx` (`Reservation_code` ASC, `Reservation_Customer_customer_id` ASC) VISIBLE,
  CONSTRAINT `fk_Reservation_has_Dining_table_Reservation1`
    FOREIGN KEY (`Reservation_code`)
    REFERENCES `Restaurant`.`Reservation` (`code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reservation_has_Dining_table_Dining_table1`
    FOREIGN KEY (`Dining_table_id`)
    REFERENCES `Restaurant`.`Dining_table` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurant`.`Bill`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Bill` (
  `id` INT NOT NULL,
  `time` DATETIME NOT NULL,
  `amount` DECIMAL NOT NULL,
  `Order_order_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_Bill_Order1_idx` (`Order_order_id` ASC) VISIBLE,
  CONSTRAINT `fk_Bill_Order1`
    FOREIGN KEY (`Order_order_id`)
    REFERENCES `Restaurant`.`Order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurant`.`Payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Payment` (
  `id` INT NOT NULL,
  `method` VARCHAR(45) NOT NULL,
  `Customer_customer_id` INT NOT NULL,
  `Bill_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idPayment_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_Payment_Customer1_idx` (`Customer_customer_id` ASC) VISIBLE,
  INDEX `fk_Payment_Bill1_idx` (`Bill_id` ASC) VISIBLE,
  CONSTRAINT `fk_Payment_Customer1`
    FOREIGN KEY (`Customer_customer_id`)
    REFERENCES `Restaurant`.`Customer` (`customer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Payment_Bill1`
    FOREIGN KEY (`Bill_id`)
    REFERENCES `Restaurant`.`Bill` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurant`.`Res_location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurant`.`Res_location` (
  `id` INT NOT NULL,
  `building` VARCHAR(45) NOT NULL,
  `street` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `zip_code` VARCHAR(45) NOT NULL,
  `Restaurant_id` INT NOT NULL,
  PRIMARY KEY (`id`, `Restaurant_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_Res_location_Restaurant1_idx` (`Restaurant_id` ASC) VISIBLE,
  CONSTRAINT `fk_Res_location_Restaurant1`
    FOREIGN KEY (`Restaurant_id`)
    REFERENCES `Restaurant`.`Restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
