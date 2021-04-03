DROP DATABASE IF EXISTS project;
CREATE DATABASE project;
USE project;

DROP TABLE IF EXISTS book
CASCADE;
DROP TABLE IF EXISTS customer
CASCADE;
DROP TABLE IF EXISTS book
CASCADE;
DROP TABLE IF EXISTS orders
CASCADE;
DROP TABLE IF EXISTS ordering
CASCADE;
DROP TABLE IF EXISTS book_author
CASCADE;

CREATE TABLE category
(
    id integer primary key,
    loan_period integer not null,
    max_books integer not null
);

CREATE TABLE user
(
    user_id varchar(10) primary key,
    name varchar(25) not null,
    address varchar(100) not null,
    category_id integer not null,
    FOREIGN KEY(category_id) REFERENCES category(id)
);

CREATE TABLE book
(
    call_number varchar(8) primary key,
    title varchar(30) not null,
    publish_date varchar(10) not null
);

CREATE TABLE copy
(
    call_number integer not null,
    copy_number integer not null,
    PRIMARY KEY(call_number, copy_number),
    FOREIGN KEY(call_number) REFERENCES book(call_number)
);

CREATE TABLE checkout_record
(
    user_id varchar(10) not null,
    call_number varchar(8) not null,
    copy_number integer not null,
    checkout_date varchar(10) not null,
    return_date varchar(10),
    PRIMARY KEY(user_id, call_number, copy_number, checkout_date),
    FOREIGN KEY(user_id) REFERENCES user(user_id),
    FOREIGN KEY(call_number, copy_number) REFERENCES copy(call_number, copy_number)
);

CREATE TABLE author
(
    name varchar(25) not null,
    call_number varchar(8) not null,
    PRIMARY KEY(name, call_number),
    FOREIGN KEY(call_number) REFERENCES book(call_number)
);

