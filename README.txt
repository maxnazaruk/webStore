docker run --name habr-pg-13.3 -p 5432:5432 -e POSTGRES_USER=user -e POSTGRES_PASSWORD=pswd -e POSTGRES_DB=webstore -d postgres:13.3

CREATE TABLE Goods (
    id int,
    name varchar(255),
    price int,
    creationDate DATE
);