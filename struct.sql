DROP DATABASE IF EXISTS project;
CREATE DATABASE project;
USE project;

DROP TABLE IF EXISTS book
CASCADE;
DROP TABLE IF EXISTS customer
CASCADE;
DROP TABLE IF EXISTS orders
CASCADE;
DROP TABLE IF EXISTS ordering
CASCADE;
DROP TABLE IF EXISTS book_author
CASCADE;

CREATE TABLE book
(
    ISBN varchar(13) PRIMARY KEY,
    title varchar(100) NOT NULL,
    unit_price integer NOT NULL CHECK(unit_price>=0),
    no_of_copies integer NOT NULL CHECK(no_of_copies>=0)
);

CREATE TABLE customer
(
    customer_id varchar(10) PRIMARY KEY,
    name varchar(50) NOT NULL,
    shipping_address varchar(200) NOT NULL,
    credit_card_no varchar(19) NOT NULL
);

CREATE TABLE orders
(
    order_id varchar(8) PRIMARY KEY,
    o_date DATE NOT NULL,
    shipping_status varchar(1) NOT NULL,
    charge integer NOT NULL CHECK(unit_price>=0),
    customer_id varchar(10) NOT NULL
);

CREATE TABLE ordering
(
    order_id varchar(8) NOT NULL,
    ISBN varchar(13) NOT NULL,
    quantity integer NOT NULL,
    PRIMARY KEY(order_id, ISBN),
    FOREIGN KEY(order_id) REFERENCES orders(order_id),
    FOREIGN KEY(call_number, copy_number) REFERENCES copy(call_number, copy_number)
);

CREATE TABLE book_author
(
    ISBN varchar(13) NOT NULL,
    author_name varchar(50) NOT NULL,
    PRIMARY KEY(ISBN, author_name),
    FOREIGN KEY(ISBN) REFERENCES book(ISBN)
);

