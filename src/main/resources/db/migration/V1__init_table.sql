drop table if exists tasks;

create table book(
    id bigint primary key auto_increment,
    isbn int not null,
    author varchar(255) not null,
    name varchar(255) not null,
    description varchar(255) not null
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;