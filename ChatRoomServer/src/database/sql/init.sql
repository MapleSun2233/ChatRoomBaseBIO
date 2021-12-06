create database chat default character set utf8;
use chat;
create table user(
    username varchar(20) primary key,
    password varchar(20) not null
);