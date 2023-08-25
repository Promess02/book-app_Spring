drop table book if exists;

create table book(
    id int primary key auto_increment,
    isbn int not null,
    name varchar(200) not null,
    author varchar(200) not null,
    description varchar(200) not null
);