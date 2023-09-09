package com.miko.bookapp;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;

import java.util.HashSet;

public class Dummy {

    public static Book dummyBook(int id, String desc){
        return new Book(id, 47,"book", "author", desc, null, null, 56.3,null);
    }

    public static BookBundle dummyBundle(int id, String desc){
        return new BookBundle(id,desc, 0.45,0d, new HashSet<>());
    }


}
