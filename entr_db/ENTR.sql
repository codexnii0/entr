/*
create database entr_db;
use entr_db;
*/

create table users (
	id int primary key auto_increment,
    name varchar(100) not null,
    email varchar(100) unique not null
    );