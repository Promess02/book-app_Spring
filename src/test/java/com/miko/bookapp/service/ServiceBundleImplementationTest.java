package com.miko.bookapp.service;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.repo.BookRepo;
import com.miko.bookapp.repo.BundleRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceBundleImplementationTest {

    @Test
    @DisplayName("checking if update Bundle returns empty Optional when id is not found")
    void updateBundleMissingID() {
        //given
        var mockBundleRepo = mock(BundleRepo.class);
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);
        //when
        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);
        //then
        assertThat(mockBundleService.updateBundle(1,dummyBundle(1,"desc"))).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checking if Bundle updates successfully")
    void updateBundle() {
        //given
        var mockBundleRepo = inMemoryBundleRepo();
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);
        //when
        //then
        mockBundleService.saveBundle(dummyBundle(1, "old"));

        if(mockBundleService.findBundleById(1).isPresent()){
            //checking if bundle saved
            assertThat(mockBundleService.findBundleById(1).get().getDescription()).isEqualTo("old");
            //checking if function returns the correct response
            assertThat(mockBundleService.updateBundle(1,dummyBundle(1,"new")).get().getDescription()).isEqualTo("new");
            //updating the in memory bundle repo
            mockBundleService.updateBundle(1,dummyBundle(1,"new"));
            //checking if the bundle updated in the repo
            assertArrayEquals(mockBundleService.findBundleById(1).get().getDescription().toCharArray(), "new".toCharArray());
        }else throw new RuntimeException("couldn't find the bundle");

    }

    @Test
    @DisplayName("checking if returns empty optional if id not found")
    void changeBundleMissingID() {
        //given
        var mockBundleRepo = mock(BundleRepo.class);
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);
        //when
        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);
        //then
        assertThat(mockBundleService.changeBundle(1,dummyBundle(1,"desc"))).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checking if successfully changes the bundle")
    void changeBundle() {
        //given
        var mockBundleRepo = inMemoryBundleRepo();
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);
        //then

        mockBundleService.saveBundle(new BookBundle(1,"old",0.34, 0d, new HashSet<>() ));

        //check if bundle saved
        assertThat(mockBundleRepo.existsById(1)).isEqualTo(true);
        //check if changeBundle returns a good response
        assertThat(mockBundleService.changeBundle(1, new BookBundle(1,"new",0.38d,null,null)).get().getDescription()).isEqualTo("new");
        //check if changeBundle saves the changed bundle to the repo
        mockBundleService.changeBundle(1, new BookBundle(1,"new",0.38d,null,null));
        if(mockBundleService.findBundleById(1).isPresent()){
            assertThat(mockBundleService.findBundleById(1).get().getDescription()).isEqualTo("new");
            assertThat(mockBundleService.findBundleById(1).get().getDiscount()).isEqualTo(0.38d);
            assertThat(mockBundleService.findBundleById(1).get().getCombinedPrize()).isEqualTo(0d);
            assertThat(mockBundleService.findBundleById(1).get().getBooks()).isEqualTo(Collections.emptySet());
        }else throw new RuntimeException("couldn't find the bundle with id 1");
    }

    @Test
    @DisplayName("checking if returns empty Optional if id not found")
    void deleteBundleByIdMissingID() {
        //given
        var mockBundleRepo = mock(BundleRepo.class);
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);

        //when
        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);

        //then
        assertThat(mockBundleService.deleteBundleById(3)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checking if deletes the bundle successfully")
    void deleteBundleById() {
        //given
        var mockBundleRepo = inMemoryBundleRepo();
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);
        //when
        //then
        mockBundleService.saveBundle(dummyBundle(1,"old"));
        //checking if bundle added
        assertThat(mockBundleService.findBundleById(1).isPresent()).isEqualTo(true);
        mockBundleService.deleteBundleById(1);
        //checking if the bundle was deleted
        assertThat(mockBundleService.findBundleById(1).isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if adds the book to the bundle successfully")
    void addBookToBundle() {
        var mockBundleRepo = inMemoryBundleRepo();
        var mockBookRepo = inMemoryBookRepo();
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);

        mockBookRepo.save(dummyBook(1, "The Last Empire"));
        mockBundleRepo.save(dummyBundle(1,"Mistborn"));

        assertThat(mockBundleService.findBundleById(1).isPresent()).isEqualTo(true);
        assertThat(mockBookRepo.findById(1).isPresent()).isEqualTo(true);

        if(mockBundleRepo.findById(1).isPresent() && mockBookRepo.findById(1).isPresent()){
            mockBundleService.addBookToBundle(1,1);
            var book = mockBookRepo.findById(1).get();
            var bundle = mockBundleRepo.findById(1).get();
            //checking if the book has a reference to the bundle
            assertThat(book.getBookBundle()).isEqualTo(bundle);
            //checking if the prize calculated correctly
            assertThat(bundle.getCombinedPrize()).isEqualTo((1-bundle.getDiscount())*book.getPrize());
            //saving a new book
            var newBook = dummyBook(2,"The Well Of Ascension");
            mockBookRepo.save(newBook);
            //checking if the book was added to bundle correctly
            mockBundleService.addBookToBundle(1,2);
            assertThat(mockBookRepo.findById(2).get().getBookBundle()).isEqualTo(bundle);
            //checking if the prize of the bundle recalculated correctly
            assertThat(mockBundleRepo.findById(1).get().getCombinedPrize()).isEqualTo((1-bundle.getDiscount())*(book.getPrize()+ newBook.getPrize()));
        }else throw new RuntimeException("book or bundle not found in the repos");

    }

    @Test
    @DisplayName("checks if returns empty Optional when missing id")
    void addBookToBundleMissingID() {
        var mockBundleRepo = mock(BundleRepo.class);
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);

        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);

        //then
        assertThat(mockBundleService.addBookToBundle(1,1)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if returns empty optional if no bundle id found")
    void deleteBookFromBundleNoBundleID() {
        var mockBundleRepo = mock(BundleRepo.class);
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);

        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);

        assertThat(mockBundleService.deleteBookFromBundle(1,1)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if returns empty optional if no book id found")
    void deleteBookFromBundleNoBookID() {
        var mockBundleRepo = mock(BundleRepo.class);
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);

        when(mockBookRepo.existsById(anyInt())).thenReturn(false);

        assertThat(mockBundleService.deleteBookFromBundle(1,1)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if deletes the book from bundle successfully")
    void deleteBookFromBundle() {
        var mockBundleRepo = inMemoryBundleRepo();
        var mockBookRepo = inMemoryBookRepo();
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);

        mockBookRepo.save(dummyBook(1,"mistborn"));
        mockBundleRepo.save(dummyBundle(1,"old"));

        assertThat(mockBundleRepo.existsById(1)).isEqualTo(true);
        assertThat(mockBookRepo.existsById(1)).isEqualTo(true);

        if(mockBookRepo.findById(1).isPresent() && mockBundleRepo.findById(1).isPresent()){
            mockBundleService.deleteBookFromBundle(1,1);
            var book = mockBookRepo.findById(1).get();
            var bundle = mockBundleRepo.findById(1).get();
            //check if book doesn't have reference to the bundle
            assertThat(book.getBookBundle()).isEqualTo(null);
            //check if bundle contains the book
            assertThat(bundle.getBooks().contains(book)).isEqualTo(false);
            //checking if the function returns the bundle without the book
            assertThat(mockBundleService.deleteBookFromBundle(1,1)).isEqualTo(Optional.of(bundle));
        }else throw new RuntimeException("couldn't find the book or the bundle in repos");
    }

    @Test
    @DisplayName("check if changes discount correctly")
    void changeBundleDiscount() {
        var mockBundleRepo = inMemoryBundleRepo();
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);

        mockBundleService.saveBundle(dummyBundle(2,"old"));
        mockBundleService.changeBundleDiscount(2,0.24);
        assertThat(mockBundleRepo.findById(2).get().getDiscount()).isEqualTo(0.24);
    }

    @Test
    @DisplayName("check if returns empty optional")
    void changeBundleDiscountMissingID() {
        var mockBundleRepo = mock(BundleRepo.class);
        var mockBookRepo = mock(BookRepo.class);
        var mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo);

        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);

        assertThat(mockBundleService.deleteBookFromBundle(1,1)).isEqualTo(Optional.empty());
    }
    private BookBundle dummyBundle(int id, String desc){
        return new BookBundle(id,desc, 0.45,0d,new HashSet<>());
    }

    private Book dummyBook(int id, String name){
        return new Book(id, 45, name, "Sanderson", null, null, null, 34.4d, null);
    }

    private MemoryBundleRepo inMemoryBundleRepo(){
        return new MemoryBundleRepo();
    }

    private MemoryBookRepo inMemoryBookRepo(){
        return new MemoryBookRepo();
    }
}