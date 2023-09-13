package com.miko.bookapp.Controller;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.FilterObject;
import com.miko.bookapp.model.Response;
import com.miko.bookapp.service.ServiceBook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final ServiceBook service;
    @GetMapping("/list")
    public ResponseEntity<Response> listBooks(){
        return ResponseUtil.okResponse("List Of Books retrieved","books",
                service.getAllBooks());
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Response> getBookById(@PathVariable long id){
        Optional<Book> result = service.findBookById(id);
        if(result.isPresent()){
            return ResponseUtil.okResponse("a book with id: " + id + " retrieved", "book",service.findBookById(id));
        }
        return ResponseUtil.idNotFoundResponse(Book.class);

    }

    @PostMapping("/create")
    public ResponseEntity<Response> createBook(@RequestBody @Valid Book book){
        try{
            return ResponseUtil.okResponse("a new book added", "book",
                    service.saveBook(book));
        }catch (IllegalArgumentException e) {
            log.info("bad enum value given");
            return badEnumResponse();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateBook(@RequestBody Book book, @PathVariable long id) {
            Optional<Book> result = service.findBookById(id);
            if (result.isPresent()) {
                return ResponseUtil.okResponse("a book with id: " + id + " updated", "book",
                        service.updateBook(id, book));
            }
            return ResponseUtil.idNotFoundResponse(Book.class);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> changeBook(@RequestBody Book book, @PathVariable long id){
        Optional<Book> result = service.findBookById(id);
        if(result.isPresent()){
                return ResponseUtil.okResponse("a book with id: " + id + " changed", "book", service.changeBook(id, book));
        }
        return ResponseUtil.idNotFoundResponse(Book.class);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteBookById(@PathVariable long id){
        Optional<Book> result = service.findBookById(id);
        if(result.isPresent()){
            return ResponseUtil.okResponse("a book with id: " + id + " deleted", "book", service.deleteBookById(id));
        }

        return ResponseUtil.idNotFoundResponse(Book.class);
    }

    @GetMapping("/filter")
    public ResponseEntity<Response> filterBooks(@RequestBody FilterObject filterObject){

        var category = filterObject.getCategory()!=null? filterObject.getCategory() : null;
        var type = filterObject.getType()!=null? filterObject.getType() : null;
        var prizeMax = filterObject.getPrize()!=null? filterObject.getPrize() : null;
        var result = service.getFilteredBooks(category, type, prizeMax);
        return ResponseUtil.okResponse("List Of filtered Books retrieved", "books",result);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public static ResponseEntity<Response> badEnumResponse(){
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .message("bad enum value given")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .data(Collections.emptyMap())
                        .build()
        );
    }

}
