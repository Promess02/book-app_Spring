package com.miko.bookapp.service;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.repo.BundleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceBundleImplementation implements ServiceBundle{

    private final BundleRepo bundleRepo;

    @Override
    public List<BookBundle> getAllBundles() {
        log.info("retrieving all bundles");
       return bundleRepo.findAll();
    }

    @Override
    public BookBundle saveBundle(BookBundle bundle) {
        log.info("bundle saved");
        return bundleRepo.save(bundle);
    }

    @Override
    public Optional<BookBundle> findBundleById(long id) {
        if(bundleRepo.existsById(id)) {
            log.info("bundle with id: " + id + " found");
            return bundleRepo.findById(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BookBundle> updateBundle(long id, BookBundle bundle) {
        if(bundleRepo.existsById(id)) {
            log.info("updated bundle with id: "+ id);
            bundleRepo.save(bundle);
            return bundleRepo.findById(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BookBundle> changeBundle(long id, BookBundle bundle) {
        if(bundleRepo.existsById(id)){
            var bundleClass = bundle.getClass();
            var bundleFields = bundleClass.getDeclaredFields();

            Optional<BookBundle> bundleOptional = bundleRepo.findById(id);
            if(bundleOptional.isEmpty()) return Optional.empty();
            BookBundle result = bundleOptional.get();

            for(Field field: bundleFields){
                try {
                    var oldValue = field.get(result);
                    var newValue = field.get(bundle);
                    if(!oldValue.equals(newValue) && !field.getName().equals("bundleID"))
                        field.set(result,newValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("bundle changed with id: " + id);
            return Optional.of(result);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BookBundle> deleteBundleById(long id) {
        if(bundleRepo.existsById(id)) {
            bundleRepo.deleteById(id);
            log.info("bundle deleted with id: " + id);
            return bundleRepo.findById(id);
        }
        else return Optional.empty();
    }

    @Override
    public Optional<BookBundle> addBookToBundle(long id, Book book) {
        if(bundleRepo.existsById(id)){
            var bundleOptional = bundleRepo.findById(id);
            if(bundleOptional.isEmpty()) return Optional.empty();
            var bundle = bundleOptional.get();

            bundle.addBook(book);
            bundleRepo.save(bundle);
            log.info("book added to a bundle");
            return Optional.of(bundle);
        }

        return Optional.empty();
    }

    @Override
    public Optional<BookBundle> deleteBookFromBundle(long bundleID, long bookID) {
        if(bundleRepo.existsById(bundleID)){
            var bundleOptional = bundleRepo.findById(bundleID);
            if(bundleOptional.isEmpty()) return Optional.empty();
            var bundle = bundleOptional.get();
            bundle.deleteBook(bookID);
            bundleRepo.save(bundle);
            log.info("book deleted from the bundle");
        }
        return Optional.empty();
    }

    @Override
    public Optional<BookBundle> changeBundleDiscount(long bundleID, double newDiscount) {
        if(bundleRepo.existsById(bundleID)){
            var bundleOptional = bundleRepo.findById(bundleID);
            if(bundleOptional.isEmpty()) return Optional.empty();
            var bundle = bundleOptional.get();

            bundle.setDiscount(newDiscount);
            bundleRepo.save(bundle);
            log.info("bundle discount changed");
        }
        return Optional.empty();
    }
}
