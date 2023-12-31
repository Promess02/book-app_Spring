package com.miko.bookapp.service.Implementation;

import com.miko.bookapp.Utils;
import com.miko.bookapp.enums.BookCategory;
import com.miko.bookapp.enums.BookType;
import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.Product;
import com.miko.bookapp.repo.BookRepo;
import com.miko.bookapp.repo.ProductRepo;
import com.miko.bookapp.service.ServiceBook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceBookImplementation implements ServiceBook {
//TODO - FIX TESTS AND IMPLEMENT THAT PRODUCTS SAVE TO DB
    private final BookRepo bookRepo;
    private final ProductRepo productRepo;
    @Override
    public List<Book> getAllBooks() {
        log.info("Retrieving all the books");
        return bookRepo.findAll();
    }

    @Override
    public Book saveBook(Book book) {
        log.info("Saving book");
        long id = book.getId();
        if(id == 0 && bookRepo.existsById(0)) id = productRepo.getNextGeneratedId();

       productRepo.saveEntity(new Product(id, book.getDescription(),book.getPrice()));
        book.setId(id);

        return bookRepo.saveEntity(book);
    }

    @Override
    public Optional<Book> findBookById(long id) {
        if(bookRepo.existsById(id)) return bookRepo.findById(id);
        else return Optional.empty();

    }

    @Override
    public Optional<Book> updateBook(long id, Book book) {
        if (bookRepo.existsById(id)){
            Optional<Book> result = Optional.of(book);
            result.get().setId(id);
            productRepo.save(new Product(id,book.getDescription(),book.getPrice()));
             bookRepo.save(result.get());
             return result;
        }else return Optional.empty();
    }

    @Override
    public Optional<Book> changeBook(long id, Book book){
        if(bookRepo.findById(id).isPresent()){
//            var bookClass = book.getClass();
            Field[] fields= Utils.extractFields(book);
            Book result = bookRepo.findById(id).get();
            for(Field field: fields){
                field.setAccessible(true);
                try{
                    var bookValue = field.get(book);
                    if(bookValue!= null && !field.getName().equals("id") && !field.getName().equals("isbn")) field.set(result, bookValue);
                }catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            productRepo.save(result);
            bookRepo.save(result);
            return Optional.of(result);
        }else return Optional.empty();
    }
    @Override
    public Optional<Book> deleteBookById(long id) {
        if (bookRepo.existsById(id)){
            productRepo.deleteById(id);
            bookRepo.deleteById(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Book>> getFilteredBooks(String category, String type, Double prizeMax) {
        var allBooks = bookRepo.findAll();
        BookCategory bookCategory = BookCategory.fromValue(category);
        BookType bookType = BookType.fromValue(type);
        // if given parameter is null then books are not filtered through with that parameter
        var result = allBooks.stream()
                .filter(book ->
                        (category == null || (book.getBookCategory() != null && book.getBookCategory().equals(bookCategory)))
                                && (type == null || (book.getBookType() != null && book.getBookType().equals(bookType)))
                                && (prizeMax == null || (book.getPrice() != null && book.getPrice() <= prizeMax))
                )
                .collect(Collectors.toList());

        if (result.isEmpty()) return Optional.empty();
        else return Optional.of(result);
    }

    @Override
    public void deleteAllBooks() {
        bookRepo.deleteAll();
        log.info("all books deleted");
    }

}
