package com.miko.bookapp.service;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.repo.BookRepo;
import com.miko.bookapp.repo.BundleRepo;
import com.miko.bookapp.repo.ProductRepo;
import com.miko.bookapp.service.Implementation.ServiceBundleImplementation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceBundleImplementationTest {
    @Mock
    private BookRepo mockBookRepo;

    @Mock
    private BundleRepo mockBundleRepo;

    @Mock
    private ProductRepo mockProductRepo;

    private ServiceBundle mockBundleService;

    private AutoCloseable autoCloseable;

    private static BookRepo memoryBookRepo;
    private static BundleRepo memoryBundleRepo;

    private static ProductRepo memoryProductRepo;
    private static ServiceBundle memoryBundleService;
    private static ServiceBundle memoryMockBundleService;

    @BeforeAll
    public static void setUp(){
        memoryBookRepo = new MemoryBookRepo();
        memoryBundleRepo = new MemoryBundleRepo();
        memoryProductRepo = new MemoryProductRepo();
        memoryBundleService = new ServiceBundleImplementation(memoryBundleRepo, memoryBookRepo, memoryProductRepo);
        var mockBookRepo = mock(BookRepo.class);
        memoryMockBundleService = new ServiceBundleImplementation(memoryBundleRepo,mockBookRepo, memoryProductRepo);
    }

    @BeforeEach
    public void setUpMock(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockBundleService = new ServiceBundleImplementation(mockBundleRepo, mockBookRepo, mockProductRepo);
    }

    private void tearDown() throws Exception {
        memoryBookRepo.deleteAll();
        memoryBundleRepo.deleteAll();
        memoryProductRepo.deleteAll();
        autoCloseable.close();
    }

    @Test
    @DisplayName("checking if update Bundle returns empty Optional when id is not found")
    void updateBundleMissingID() {
        //given
        //when
        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);
        //then
        assertThat(mockBundleService.updateBundle(1,dummyBundle(1,"desc"))).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if returns all bundles")
    void findAllBundles() {

        memoryBundleRepo.save(dummyBundle(1, "bundle1"));
        memoryBundleRepo.save(dummyBundle(2, "bundle2"));
        memoryBundleRepo.save(dummyBundle(3, "bundle3"));
        memoryBundleRepo.save(dummyBundle(4, "bundle4"));

        //checks if function finds any bundles
        assertThat(memoryMockBundleService.getAllBundles().isEmpty()).isEqualTo(false);
        //checks if function finds all the bundles saved in repository
        assertThat(memoryMockBundleService.getAllBundles().size()).isEqualTo(4);
    }
    @Test
    @DisplayName("checking if Bundle updates successfully")
    void updateBundle() {
        //given
        //when
        //then
        memoryMockBundleService.saveBundle(dummyBundle(1, "old"));

        if(memoryMockBundleService.findBundleById(1).isPresent()){
            //checking if bundle saved
            assertThat(memoryMockBundleService.findBundleById(1).get().getDescription()).isEqualTo("old");
            //checking if function returns the correct response
            assertThat(memoryMockBundleService.updateBundle(1,dummyBundle(1,"new")).get().getDescription()).isEqualTo("new");
            //updating the in memory bundle repo
            memoryMockBundleService.updateBundle(1,dummyBundle(1,"new"));
            //checking if the bundle updated in the repo
            assertThat(memoryProductRepo.findById(1).get().getDescription()).isEqualTo("new");
            assertThat(memoryBundleRepo.findById(1).get().getDescription()).isEqualTo("new");
        }else throw new RuntimeException("couldn't find the bundle");

    }

    @Test
    @DisplayName("checking if returns empty optional if id not found")
    void changeBundleMissingID() {
        //given
        //when
        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);
        //then
        assertThat(mockBundleService.changeBundle(1,dummyBundle(1,"desc"))).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checking if successfully changes the bundle")
    void changeBundle() {
        //given
        //then
        memoryMockBundleService.saveBundle(new BookBundle(2,"old",0.34, 0d, new HashSet<>() ));
        //check if bundle saved
        assertThat(memoryBundleRepo.existsById(2)).isEqualTo(true);
        //check if changeBundle returns a good response
        assertThat(memoryMockBundleService.changeBundle(2, new BookBundle(2,"new",0.38d,null,null)).get().getDescription()).isEqualTo("new");
        //check if changeBundle saves the changed bundle to the repo
        memoryMockBundleService.changeBundle(2, new BookBundle(2,"new",0.38d,null,null));
        if(memoryMockBundleService.findBundleById(2).isPresent()){
            var bookBundle = memoryMockBundleService.findBundleById(2).get();
            assertThat(bookBundle.getDescription()).isEqualTo("new");
            assertThat(bookBundle.getDescription()).isEqualTo("new");
            assertThat(bookBundle.getDiscount()).isEqualTo(0.38d);
            assertThat(bookBundle.getPrice()).isEqualTo(0d);
            assertThat(bookBundle.getBooks()).isEqualTo(Collections.emptySet());
        }else throw new RuntimeException("couldn't find the bundle with id 1");
    }

    @Test
    @DisplayName("checking if returns empty Optional if id not found")
    void deleteBundleByIdMissingID() {
        //when
        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);
        //then
        assertThat(mockBundleService.deleteBundleById(3)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checking if deletes the bundle successfully")
    void deleteBundleById() {
        //given
        //when
        //then
        memoryMockBundleService.saveBundle(dummyBundle(1,"old"));
        //checking if bundle added
        assertThat(memoryMockBundleService.findBundleById(1).isPresent()).isEqualTo(true);
        memoryMockBundleService.deleteBundleById(1);
        //checking if the bundle was deleted
        assertThat(memoryMockBundleService.findBundleById(1).isPresent()).isEqualTo(false);
        assertThat(memoryProductRepo.findById(1).isPresent()).isEqualTo(false);
        assertThat(memoryBookRepo.findById(1).isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if adds the book to the bundle successfully")
    void addBookToBundle() {

        memoryBookRepo.save(dummyBook(1, "The Last Empire"));
        memoryBundleRepo.save(dummyBundle(1,"Mistborn"));

        assertThat(memoryBundleService.findBundleById(1).isPresent()).isEqualTo(true);
        assertThat(memoryBookRepo.findById(1).isPresent()).isEqualTo(true);

        if(memoryBundleRepo.findById(1).isPresent() && memoryBookRepo.findById(1).isPresent()){
            memoryBundleService.addBookToBundle(1,1);
            var book = memoryBookRepo.findById(1).get();
            var bundle = memoryBundleRepo.findById(1).get();
            //checking if the book has a reference to the bundle
            assertThat(book.getBookBundle()).isEqualTo(bundle);
            //checking if the prize calculated correctly
            assertThat(bundle.getPrice()).isEqualTo((1-bundle.getDiscount())*book.getPrice());
            //saving a new book
            var newBook = dummyBook(2,"The Well Of Ascension");
            memoryBookRepo.save(newBook);
            //checking if the book was added to bundle correctly
            memoryBundleService.addBookToBundle(1,2);
            assertThat(memoryBookRepo.findById(2).get().getBookBundle()).isEqualTo(bundle);
            //checking if the prize of the bundle recalculated correctly
            assertThat(memoryBundleRepo.findById(1).get().getPrice()).isEqualTo((1-bundle.getDiscount())*(book.getPrice()+ newBook.getPrice()));
        }else throw new RuntimeException("book or bundle not found in the repos");

    }

    @Test
    @DisplayName("checks if returns empty Optional when missing id")
    void addBookToBundleMissingID() {

        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);

        //then
        assertThat(mockBundleService.addBookToBundle(1,1)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if returns empty optional if no bundle id found")
    void deleteBookFromBundleNoBundleID() {

        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);

        assertThat(mockBundleService.deleteBookFromBundle(1,1)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if returns empty optional if no book id found")
    void deleteBookFromBundleNoBookID() {

        when(mockBookRepo.existsById(anyInt())).thenReturn(false);

        assertThat(mockBundleService.deleteBookFromBundle(1,1)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if deletes the book from bundle successfully")
    void deleteBookFromBundle() {

        memoryBookRepo.save(dummyBook(1,"mistborn"));
        memoryBundleRepo.save(dummyBundle(1,"old"));

        assertThat(memoryBundleRepo.existsById(1)).isEqualTo(true);
        assertThat(memoryBookRepo.existsById(1)).isEqualTo(true);

        if(memoryBookRepo.findById(1).isPresent() && memoryBundleRepo.findById(1).isPresent()){
            memoryBundleService.deleteBookFromBundle(1,1);
            var book = memoryBookRepo.findById(1).get();
            var bundle = memoryBundleRepo.findById(1).get();
            //check if book doesn't have reference to the bundle
            assertThat(book.getBookBundle()).isEqualTo(null);
            //check if bundle contains the book
            assertThat(bundle.getBooks().contains(book)).isEqualTo(false);
            //checking if the function returns the bundle without the book
            assertThat(memoryBundleService.deleteBookFromBundle(1,1)).isEqualTo(Optional.of(bundle));
        }else throw new RuntimeException("couldn't find the book or the bundle in repos");
    }

    @Test
    @DisplayName("check if changes discount correctly")
    void changeBundleDiscount() {

        memoryMockBundleService.saveBundle(dummyBundle(2,"old"));
        memoryMockBundleService.changeBundleDiscount(2,0.24);
        assertThat(memoryBundleRepo.findById(2).get().getDiscount()).isEqualTo(0.24);
    }

    @Test
    @DisplayName("check if returns empty optional")
    void changeBundleDiscountMissingID() {

        when(mockBundleRepo.existsById(anyInt())).thenReturn(false);

        assertThat(mockBundleService.deleteBookFromBundle(1,1)).isEqualTo(Optional.empty());
    }
    private BookBundle dummyBundle(int id, String desc){
        return new BookBundle(id,desc, 0.45,0d, new HashSet<>());
    }

    private Book dummyBook(int id, String name){
        return new Book(id, 45, name, "Sanderson", null, null, null, 34.4d, null);
    }
}