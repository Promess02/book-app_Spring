package com.miko.bookapp.service;

import com.miko.bookapp.model.BookBundle;

import java.util.List;
import java.util.Optional;

public interface ServiceBundle {

    List<BookBundle> getAllBundles();
    BookBundle saveBundle(BookBundle bundle);
    Optional<BookBundle> findBundleById(long id);
    Optional<BookBundle> updateBundle(long id, BookBundle bundle);

    Optional<BookBundle> changeBundle(long id, BookBundle bundle);

    Optional<BookBundle> deleteBundleById(long id);

    Optional<BookBundle> addBookToBundle(long BundleID, long BookID);

    Optional<BookBundle> deleteBookFromBundle(long bundleID, long bookID);

    Optional<BookBundle> changeBundleDiscount(long bundleID, double newDiscount);
}
