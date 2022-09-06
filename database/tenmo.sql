BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transfer;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
    INCREMENT BY 1
    START WITH 1001
    NO MAXVALUE;

CREATE TABLE tenmo_user
(
    user_id       int          NOT NULL DEFAULT nextval('seq_user_id'),
    username      varchar(50)  NOT NULL,
    password_hash varchar(200) NOT NULL,
    CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
    CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
    INCREMENT BY 1
    START WITH 2001
    NO MAXVALUE;

CREATE TABLE account
(
    account_id int            NOT NULL DEFAULT nextval('seq_account_id'),
    user_id    int            NOT NULL,
    balance    decimal(13, 2) NOT NULL,
    CONSTRAINT PK_account PRIMARY KEY (account_id),
    CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);
CREATE SEQUENCE seq_transfer_id
    INCREMENT BY 1
    START WITH 3001
    NO MAXVALUE;

CREATE TABLE transfer
(
    transfer_id  int            NOT NULL DEFAULT nextval('seq_transfer_id'),
    account_from int            NOT NULL,
    account_to   int            NOT NULL,
    amount       decimal(13, 2) NOT NULL,
    transfer_status varchar(20) DEFAULT ('Approved'),
    CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
    CONSTRAINT FK_transfer_account_from FOREIGN KEY (account_from) REFERENCES account (account_id),
    CONSTRAINT FK_transfers_accounts_to FOREIGN KEY (account_to) REFERENCES account (account_id),
    -- CONSTRAINT CK_transfers_not_same_account CHECK  (account_from<>account_to),
    CONSTRAINT CK_transfers_amount_gt_0 CHECK (amount>0)
);

COMMIT;

--
-- INSERT INTO tenmo_user (user_id, username, password_hash) VALUES (1002, 'user1', 'pass1');
-- INSERT INTO tenmo_user (user_id, username, password_hash) VALUES (1003, 'user2', 'pass2');
-- INSERT INTO tenmo_user (user_id, username, password_hash) VALUES (1004, 'user4', 'pass3');
-- INSERT INTO tenmo_user (user_id, username, password_hash) VALUES (1005, 'user5', 'pass4');
-- INSERT INTO tenmo_user (user_id, username, password_hash) VALUES (1006, 'user6', 'pass5');
--
-- INSERT INTO account (account_id, user_id, balance) VALUES (2002, 1002, 1000);
-- INSERT INTO account (account_id, user_id, balance) VALUES (2003, 1003, 1000);
-- INSERT INTO account (account_id, user_id, balance) VALUES (2004, 1004, 1000);
-- INSERT INTO account (account_id, user_id, balance) VALUES (2005, 1005, 1000);
-- INSERT INTO account (account_id, user_id, balance) VALUES (2006, 1006, 1000);

-- Select * FROM tenmo_user

--  UPDATE account SET balance = balance - 2 WHERE account_id = 2002;
--
select