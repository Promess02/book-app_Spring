package com.miko.bookapp.Controller;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.model.Response;
import com.miko.bookapp.service.ServiceBundle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/bundles")
public class BundleController {
    ServiceBundle service;

    public BundleController(ServiceBundle serviceBundle) {
        this.service = serviceBundle;
    }
    @GetMapping("/list/{id}")
    public ResponseEntity<Response> getBundleById(@PathVariable long id){
        Optional<BookBundle> result = service.findBundleById(id);
        if(result.isPresent()){
            return ResponseEntity.ok(
                    Response.builder()
                            .message("a bundle with id: " + id + " retrieved")
                            .timestamp(LocalDateTime.now())
                            .httpStatus(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(Map.of("bundle: ", result.get()))
                            .build()
            );
        }
        return idNotFoundResponse();

    }
    @GetMapping("/list")
    public ResponseEntity<Response> listBundles(){
        return ResponseEntity.ok(
                Response.builder()
                        .message("List Of Bundles retrieved")
                        .timestamp(LocalDateTime.now())
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("bundles: ", service.getAllBundles()))
                        .build()
        );
    }



    @PostMapping("/create")
    public ResponseEntity<Response> createBundle(@RequestBody @Valid BookBundle bookBundle){
        return ResponseEntity.ok(
                Response.builder()
                        .message("a new book bundle added")
                        .timestamp(LocalDateTime.now())
                        .httpStatus(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .data(Map.of("bundle: ", service.saveBundle(bookBundle)))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateBundle(@RequestBody BookBundle bundle, @PathVariable long id){
        Optional<BookBundle> result = service.findBundleById(id);
        if(result.isPresent()){
            return ResponseEntity.ok(
                    Response.builder()
                            .message("a bundle with id: " + id + " updated")
                            .timestamp(LocalDateTime.now())
                            .httpStatus(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(Map.of("bundle: ", service.updateBundle(id, bundle)))
                            .build()
            );
        }
        return idNotFoundResponse();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> changeBundle(@RequestBody BookBundle bundle, @PathVariable long id){
        Optional<BookBundle> result = service.findBundleById(id);
        if(result.isPresent()){
            return ResponseEntity.ok(
                    Response.builder()
                            .message("a bundle with id: " + id + " changed")
                            .timestamp(LocalDateTime.now())
                            .httpStatus(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(Map.of("bundle: ", service.changeBundle(id, bundle)))
                            .build()
            );
        }
        return idNotFoundResponse();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteBundleById(@PathVariable long id){
        Optional<BookBundle> result = service.findBundleById(id);
        if(result.isPresent()){
            return ResponseEntity.ok(
                    Response.builder()
                            .message("a bundle with id: " + id + " deleted")
                            .timestamp(LocalDateTime.now())
                            .httpStatus(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(Map.of("bundle: ", service.deleteBundleById(id)))
                            .build()
            );
        }

        return idNotFoundResponse();
    }

    @PatchMapping("/addBook/{bundleID}/{bookID}")
    public ResponseEntity<Response> addBookToBundle(@PathVariable long bookID, @PathVariable long bundleID){
        Optional<BookBundle> result = service.findBundleById(bundleID);
        if(result.isPresent()){
            return ResponseEntity.ok(
                    Response.builder()
                            .message("added a book to bundle with id: " + bundleID)
                            .timestamp(LocalDateTime.now())
                            .httpStatus(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(Map.of("bundle: ", service.addBookToBundle(bundleID, bookID)))
                            .build()
            );
        }
        return idNotFoundResponse();
    }

    @PatchMapping("/deleteBook/{bookID}/{bundleID}")
    public ResponseEntity<Response> deleteBookFromBundle(@PathVariable long bookID, @PathVariable long bundleID){
        Optional<BookBundle> result = service.findBundleById(bundleID);
        if(result.isPresent()){
            return ResponseEntity.ok(
                    Response.builder()
                            .message("deleted a book with id: " + bookID + " from bundle with id: " + bundleID)
                            .timestamp(LocalDateTime.now())
                            .httpStatus(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(Map.of("bundle: ", service.deleteBookFromBundle(bundleID, bookID)))
                            .build()
            );
        }
        return idNotFoundResponse();
    }

    @PatchMapping("/discount/{id}/{value}")
    public ResponseEntity<Response> changeBundleDiscount(@PathVariable long id, @PathVariable double value){
        Optional<BookBundle> result = service.findBundleById(id);
        if(result.isPresent()){
            return ResponseEntity.ok(
                Response.builder()
                        .message("changed discount value of bundle with id: " + id + " to "+ value)
                        .timestamp(LocalDateTime.now())
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("bundle: ", service.changeBundleDiscount(id, value)))
                        .build()
            );
        }
        return idNotFoundResponse();
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
}
