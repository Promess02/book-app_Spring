create table book_order(
    orderID int primary key auto_increment,
    bookID bigint not null,
    timestamp datetime not null,
    foreign key (bookID) references book(id)
);