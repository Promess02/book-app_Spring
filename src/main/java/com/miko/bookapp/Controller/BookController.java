package com.miko.bookapp.Controller;

import com.miko.bookapp.enums.BookCategory;
import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.Response;
import com.miko.bookapp.service.ServiceBook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
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
        return idNotFoundResponse();

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
    public ResponseEntity<Response> updateBook(@RequestBody Book book, @PathVariable long id){
        Optional<Book> result = service.findBookById(id);
        if(result.isPresent()){
            try{
                return ResponseEntity.ok(
                        Response.builder()
                                .message("a book with id: " + id + " updated")
                                .timestamp(LocalDateTime.now())
                                .httpStatus(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .data(Map.of("books: ", service.updateBook(id, book)))
                                .build()
                );
            }catch (IllegalArgumentException e){
                log.info("bad enum value given");
                return badEnumResponse();
            }

        }
        return idNotFoundResponse();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> changeBook(@RequestBody Book book, @PathVariable long id){
        Optional<Book> result = service.findBookById(id);
        if(result.isPresent()){
            try{
                return ResponseEntity.ok(
                        Response.builder()
                                .message("a book with id: " + id + " changed")
                                .timestamp(LocalDateTime.now())
                                .httpStatus(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .data(Map.of("books: ", service.changeBook(id, book)))
                                .build()
                );
            }catch (IllegalArgumentException e){
                log.info("bad enum value given");
                return badEnumResponse();
            }
        }
        return idNotFoundResponse();
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

        return idNotFoundResponse();
    }

    @GetMapping("/filter/{category}/{type}/{prizeMax}")
    public ResponseEntity<Response> filterBooks(@PathVariable String category,
                                                @PathVariable String type,
                                                @PathVariable Double prizeMax){
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

    ResponseEntity<Response> idNotFoundResponse(){
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .message("id not found")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    ResponseEntity<Response> badEnumResponse(){
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .message("bad enum value given")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }



}
