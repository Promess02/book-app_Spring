package com.miko.bookapp.Controller;

import com.miko.bookapp.Utils;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final ServiceBook service;

    @GetMapping("/list")
    public ResponseEntity<Response> listBooks(){
        return ResponseEntity.ok(
                Response.builder()
                        .message("List Of Books retrieved")
                        .timestamp(LocalDateTime.now())
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("books: ", service.getAllBooks()))
                        .build()
        );
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Response> getBookById(@PathVariable long id){
        Optional<Book> result = service.findBookById(id);
        if(result.isPresent()){
            return ResponseEntity.ok(
                    Response.builder()
                            .message("a book with id: " + id + " retrieved")
                            .timestamp(LocalDateTime.now())
                            .httpStatus(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(Map.of("books: ", service.findBookById(id)))
                            .build()
            );
        }
        return Utils.idNotFoundResponse(Book.class);

    }

    @PostMapping("/create")
    public ResponseEntity<Response> createBook(@RequestBody @Valid Book book){
        try{
            return ResponseEntity.ok(
                    Response.builder()
                            .message("a new book added")
                            .timestamp(LocalDateTime.now())
                            .httpStatus(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .data(Map.of("books: ", service.saveBook(book)))
                            .build()
            );
        }catch (IllegalArgumentException e) {
            log.info("bad enum value given");
            return badEnumResponse();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateBook(@RequestBody Book book, @PathVariable long id) {
            Optional<Book> result = service.findBookById(id);
            if (result.isPresent()) {
                return ResponseEntity.ok(
                        Response.builder()
                                .message("a book with id: " + id + " updated")
                                .timestamp(LocalDateTime.now())
                                .httpStatus(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .data(Map.of("books: ", service.updateBook(id, book)))
                                .build()
                );
            }
            return Utils.idNotFoundResponse(Book.class);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> changeBook(@RequestBody Book book, @PathVariable long id){
        Optional<Book> result = service.findBookById(id);
        if(result.isPresent()){
                return ResponseEntity.ok(
                        Response.builder()
                                .message("a book with id: " + id + " changed")
                                .timestamp(LocalDateTime.now())
                                .httpStatus(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .data(Map.of("books: ", service.changeBook(id, book)))
                                .build()
                );
        }
        return Utils.idNotFoundResponse(Book.class);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteBookById(@PathVariable long id){
        Optional<Book> result = service.findBookById(id);
        if(result.isPresent()){
            return ResponseEntity.ok(
                    Response.builder()
                            .message("a book with id: " + id + " deleted")
                            .timestamp(LocalDateTime.now())
                            .httpStatus(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(Map.of("books: ", service.deleteBookById(id)))
                            .build()
            );
        }

        return Utils.idNotFoundResponse(Book.class);
    }

    @GetMapping("/filter")
    public ResponseEntity<Response> filterBooks(@RequestBody FilterObject filterObject){

        var category = filterObject.getCategory()!=null? filterObject.getCategory() : null;
        var type = filterObject.getType()!=null? filterObject.getType() : null;
        var prizeMax = filterObject.getPrize()!=null? filterObject.getPrize() : null;
        return ResponseEntity.ok(
                Response.builder()
                        .message("List Of filtered Books retrieved")
                        .timestamp(LocalDateTime.now())
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("books: ", service.getFilteredBooks(category, type, prizeMax)))
                        .build()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Response> badEnumResponse(){
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
