CREATE SCHEMA IF NOT EXISTS BANK;
USE BANK;

-------- Customer----------------
CREATE TABLE Customer (
    id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    mailing_address VARCHAR(200),
    permanent_address VARCHAR(200),
    primary_email VARCHAR(200),
    primary_phone INT(10),
    PRIMARY KEY(id),
    UNIQUE(id)
);

-------- Account_details-----------------
CREATE TABLE Account_details (
    account_number INT NOT NULL,
    account_balance DECIMAL(10,2) NOT NULL,
    customer_id INT NOT NULL,
    PRIMARY KEY(account_number),
    UNIQUE(account_number ),
	CONSTRAINT fk_Customer1
		FOREIGN KEY (customer_id)
        REFERENCES Customer(id)
	);
    
CREATE TABLE Account_transfer_details (
    id INT NOT NULL,
    account_number INT NOT NULL,
    date_of_transfer DATETIME NOT NULL,
    recipient_name VARCHAR(100) NOT NULL,
    status ENUM('waiting', 'accepted', 'declined'),
    PRIMARY KEY(id),
    UNIQUE(id),
    CONSTRAINT fk_Account_details1
		FOREIGN KEY (account_number)
        REFERENCES Account_details(account_number)
	);


SET autocommit = 0;
INSERT INTO Customer values (200, 'jas', '309, Halifax', 'B34GEG', 'jas@gfgg.com', 763442543);
INSERT INTO Account_details values (1234567890, 1000.00, 200);
INSERT INTO Account_transfer_details values (12, 1234567890, current_timestamp(), 'michael', 'waiting');
INSERT INTO Account_transfer_details values (13, 1234567890, current_timestamp(), 'john', 'waiting');

DELIMITER //    
CREATE PROCEDURE transaction_status_handling(
	IN transfer_id INT,
	IN new_status VARCHAR(20),
	IN account_number_provided INT,
    IN tranaction_amount DECIMAL(5,2)
)
BEGIN

	START TRANSACTION;
	SAVEPOINT previous_account_balance;
	UPDATE Account_details SET account_balance = account_balance - tranaction_amount where account_number = account_number_provided;

	IF new_status = 'accepted' THEN
		UPDATE Account_transfer_details SET status = new_status where id = transfer_id and account_number = account_number_provided;
		RELEASE SAVEPOINT previous_account_balance;
		COMMIT;
	
	ELSEIF new_status = 'declined' THEN
		ROLLBACK TO SAVEPOINT previous_account_balance;
		UPDATE Account_transfer_details SET status = new_status where id = transfer_id and account_number = account_number_provided;

	END IF;

	COMMIT;
END //
DELIMITER ;

#----- if status is declined-------
CALL transaction_status_handling(12, 'declined', 1234567890, 50.00);

----- if status is accepted-------
CALL transaction_status_handling(13, 'accepted', 1234567890, 50.00);

select * from account_details;
select * from account_transfer_details;