alter table book
    modify column prize double;

alter table book
    modify column bundle_id bigint null;
alter table book
    add foreign key (bundle_id) references book_bundle(bundle_id);