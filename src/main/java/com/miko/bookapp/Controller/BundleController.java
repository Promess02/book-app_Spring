package com.miko.bookapp.Controller;

import com.miko.bookapp.Utils;
import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.DTO.Response;
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
        return result.map(bookBundle -> ResponseUtil.okResponse("a bundle with id: " + id + " retrieved", "bundle", bookBundle))
                .orElseGet(() -> ResponseUtil.idNotFoundResponse(BookBundle.class));

    }
    @GetMapping("/list")
    public ResponseEntity<Response> listBundles(){
        return ResponseUtil.okResponse("List Of Bundles retrieved", "bundles", service.getAllBundles());
    }

    @GetMapping("/listBooks/{id}")
    public ResponseEntity<Response> listBooksInBundle(@PathVariable long id){
        var result = service.getListOfBooksInBundle(id);

        if (result.getMessage().equals(Utils.ID_NOT_FOUND)) return ResponseUtil.idNotFoundResponse(BookBundle.class);
        if(result.getMessage().equals(Utils.NO_BOOKS_FOUND)) return ResponseUtil.badRequestResponse(Utils.NO_BOOKS_FOUND);
        if(result.getData().isPresent()) return ResponseUtil.okResponse("List Of Books For Bundle with id: " + id + " retrieved", "books", result.getData().get());
        return ResponseUtil.somethingWentWrongResponse(result.getMessage());
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createBundle(@RequestBody @Valid BookBundle bookBundle){
        var result = service.saveBundle(bookBundle);
        return ResponseUtil.okResponse("a new book bundle added", "bundle", result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateBundle(@RequestBody BookBundle bundle, @PathVariable long id){
        Optional<BookBundle> result = service.findBundleById(id);
        if(result.isPresent()){
            return ResponseUtil.okResponse("a bundle with id: " + id + " updated",
                    "bundle", service.updateBundle(id, bundle));
        }
        return ResponseUtil.idNotFoundResponse(BookBundle.class);
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
        return ResponseUtil.idNotFoundResponse(BookBundle.class);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteBundleById(@PathVariable long id){
        Optional<BookBundle> result = service.findBundleById(id);
        if(result.isPresent()){
            return ResponseUtil.okResponse("a bundle with id: " + id + " deleted",
                    "bundle", service.deleteBundleById(id));
        }
        return ResponseUtil.idNotFoundResponse(BookBundle.class);
    }

    @PatchMapping("/addBook/{bundleID}/{bookID}")
    public ResponseEntity<Response> addBookToBundle(@PathVariable long bookID, @PathVariable long bundleID){
        Optional<BookBundle> result = service.findBundleById(bundleID);
        if(result.isPresent()){
            return ResponseUtil.okResponse("added a book to bundle with id: " + bundleID,
                    "bundle", service.addBookToBundle(bundleID, bookID));
        }
        return ResponseUtil.idNotFoundResponse(BookBundle.class);
    }

    @PatchMapping("/deleteBook/{bundleID}/{bookID}")
    public ResponseEntity<Response> deleteBookFromBundle(@PathVariable long bookID, @PathVariable long bundleID){
        Optional<BookBundle> result = service.findBundleById(bundleID);

        if(result.isPresent()){
            return ResponseUtil.okResponse("deleted a book with id: " + bookID + " from bundle with id: " + bundleID,
                    "bundle", service.deleteBookFromBundle(bundleID, bookID));
        }
        return ResponseUtil.idNotFoundResponse(BookBundle.class);
    }

    @PatchMapping("/discount/{id}/{value}")
    public ResponseEntity<Response> changeBundleDiscount(@PathVariable long id, @PathVariable double value){
        Optional<BookBundle> result = service.findBundleById(id);
        if(result.isPresent()){
            return ResponseUtil.okResponse("changed discount value of bundle with id: " + id + " to "+ value,
                    "bundle", service.changeBundleDiscount(id, value));
        }
        return ResponseUtil.idNotFoundResponse(BookBundle.class);
    }
}
