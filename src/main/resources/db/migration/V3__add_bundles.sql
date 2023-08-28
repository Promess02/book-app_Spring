alter table book
    add column prize double(5,2);

create table book_bundle(
    bundle_id int primary key auto_increment,
    description varchar(255),
    discount double(5,2) not null,
    combinedPrize double(10,2) not null,
);

alter table book
    add column book_bundle_id int null;
alter table book
    add foreign key (book_bundle_id) references book_bundle(bundle_id);