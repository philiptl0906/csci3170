DROP DATABASE IF EXISTS project;
CREATE DATABASE project;
USE project;

DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS ordering;
DROP TABLE IF EXISTS book_author;

CREATE TABLE book
(
    ISBN varchar(13) PRIMARY KEY,
    title varchar(100) NOT NULL,
    unit_price integer NOT NULL,
    no_of_copies integer NOT NULL,
    CHECK(unit_price>=0 AND no_of_copies>=0)
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
    charge integer NOT NULL,
    customer_id varchar(10) NOT NULL,
    CHECK(charge>=0)
);

CREATE TABLE ordering
(
    order_id varchar(8) NOT NULL,
    ISBN varchar(13) NOT NULL,
    quantity integer NOT NULL,
    CHECK(quantity>=0),
    PRIMARY KEY(order_id, ISBN),
    FOREIGN KEY(order_id) REFERENCES orders(order_id)
);

CREATE TABLE book_author
(
    ISBN varchar(13) NOT NULL,
    author_name varchar(50) NOT NULL,
    PRIMARY KEY(ISBN, author_name),
    FOREIGN KEY(ISBN) REFERENCES book(ISBN)
);

SELECT b.ISBN, b.title, b.no_of_copies
FROM book b
WHERE b.ISBN IN 
    (SELECT ISBN
FROM(
        SELECT ISBN, Total_no
    FROM (
            SELECT ISBN, Sum(quantity) AS Total_no
        FROM ordering
        GROUP BY ISBN
        HAVING Total_no>0)Total_result
    ORDER BY Total_no)Top_result)
LIMIT 3;
-- INSERT INTO book VALUES ('1-1234-1234-1', 'Database I',100,50);
-- INSERT INTO book VALUES ('2-2345-2345-2', 'Database II',110,40);
-- INSERT INTO book VALUES ('3-3456-3456-3', 'Operating System',130,20);
-- INSERT INTO book VALUES ('4-4567-4567-4', 'Programming in C language',140,10);
-- INSERT INTO book VALUES ('5-5678-5678-5', 'Programming in Java language',150,5);
-- INSERT INTO book VALUES ('5-5555-5555-5', 'Test for 0 quantity',150,5);
-- INSERT INTO book VALUES ('3-2345-2345-2', 'Database III',110,40);

-- customer
-- INSERT INTO customer VALUES ('adafu', 'Ada','222,Shatin,Hong Kong','4444-4444-4444-4444');
-- INSERT INTO customer VALUES ('cwwong', 'Raymond','123,Shatin,Hong Kong','1234-1234-1234-1234');
-- INSERT INTO customer VALUES ('hndai', 'Henry','222,Shatin,Hong Kong','4444-4444-4444-4444');
-- INSERT INTO customer VALUES ('hyyue','Willy','234,Kwai Chung,Hong Kong','4321-4321-4321-4321');
-- INSERT INTO customer VALUES ('raymond','Raymond Wong','999,Tai Wai,Hong Kong','9999-9999-9999-9999');
-- INSERT INTO customer VALUES ('twleung','Oscar','890,Tin Shui Wai,Hong Kong','2222-2222-2222-2222');
-- INSERT INTO customer VALUES ('wcliew','Alan','333,Tsuen Wan,Hong Kong','5555-5555-5555-5555');
-- INSERT INTO customer VALUES ('xcai','Teresa','111,Shatin,Hong Kong','3333-3333-3333-3333');

-- orders
-- INSERT INTO orders VALUES ('00000000','2005-09-01','Y',120,'cwwong');
-- INSERT INTO orders VALUES ('00000001','2005-09-02','Y',120,'cwwong');
-- INSERT INTO orders VALUES ('00000002','2005-09-07','N',120,'hyyue');
-- INSERT INTO orders VALUES ('00000003','2005-09-10','N',120,'hyyue');
-- INSERT INTO orders VALUES ('00000004','2005-09-12','N',120,'hndai');
-- INSERT INTO orders VALUES ('00000005','2005-09-20','N',120,'hndai');
-- INSERT INTO orders VALUES ('00000006','2005-09-30','N',120,'twleung');
-- INSERT INTO orders VALUES ('00000007','2005-10-01','Y',130,'twleung');
-- INSERT INTO orders VALUES ('00000008','2005-10-06','Y',130,'xcai');
-- INSERT INTO orders VALUES ('00000009','2005-10-09','Y',130,'xcai');
-- INSERT INTO orders VALUES ('00000010','2005-10-13','Y',320,'xcai');
-- INSERT INTO orders VALUES ('11111111','2099-10-13','N',320,'xcai');
-- INSERT INTO orders VALUES ('11111112','2099-10-13','Y',110,'xcai');

-- ordering
-- INSERT INTO ordering VALUES ('00000000','1-1234-1234-1',1);
-- INSERT INTO ordering VALUES ('00000001','1-1234-1234-1',1);
-- INSERT INTO ordering VALUES ('00000002','1-1234-1234-1',1);
-- INSERT INTO ordering VALUES ('00000003','1-1234-1234-1',1);
-- INSERT INTO ordering VALUES ('00000004','1-1234-1234-1',1);
-- INSERT INTO ordering VALUES ('00000005','1-1234-1234-1',1);
-- INSERT INTO ordering VALUES ('00000006','1-1234-1234-1',1);
-- INSERT INTO ordering VALUES ('00000007','2-2345-2345-2',1);
-- INSERT INTO ordering VALUES ('00000008','2-2345-2345-2',1);
-- INSERT INTO ordering VALUES ('00000009','2-2345-2345-2',1);
-- INSERT INTO ordering VALUES ('00000010','4-4567-4567-4',1);
-- INSERT INTO ordering VALUES ('00000010','5-5678-5678-5',1);
-- INSERT INTO ordering VALUES ('11111111','5-5555-5555-5',0);
-- INSERT INTO ordering VALUES ('11111112','3-2345-2345-2',1);


-- book author
-- INSERT INTO book_author VALUES ('1-1234-1234-1', 'Ada');
-- INSERT INTO book_author VALUES ('1-1234-1234-1', 'Raymond');
-- INSERT INTO book_author VALUES ('1-1234-1234-1', 'Willy');
-- INSERT INTO book_author VALUES ('2-2345-2345-2', 'Henry');
-- INSERT INTO book_author VALUES ('2-2345-2345-2', 'Teresa');
-- INSERT INTO book_author VALUES ('3-3456-3456-3', 'Ada');
-- INSERT INTO book_author VALUES ('3-3456-3456-3', 'Alan');
-- INSERT INTO book_author VALUES ('4-4567-4567-4', 'Magic');
-- INSERT INTO book_author VALUES ('5-5678-5678-5', 'Oscar');