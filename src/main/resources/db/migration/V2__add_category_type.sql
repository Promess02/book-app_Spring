
alter table book
    modify column book_category enum('BIOGRAPHY','CRIME','DRAMA','FANTASY','HORROR','ROMANCE','SCIENCE_FICTION');
alter table book
    modify column book_type enum('AUDIOBOOK','EBOOK','HARDCOVER','PAPERBACK');