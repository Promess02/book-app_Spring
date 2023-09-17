package com.miko.bookapp.service;

import com.miko.bookapp.enums.BookCategory;
import com.miko.bookapp.enums.BookType;
import com.miko.bookapp.model.Book;
import com.miko.bookapp.repo.BookRepo;
import com.miko.bookapp.repo.ProductRepo;
import com.miko.bookapp.service.Implementation.ServiceBookImplementation;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class serviceBookImplementationTest {
    private static BookRepo bookRepo;
    private static ServiceBook bookService;
    private static ProductRepo productRepo;

    @Mock
    private BookRepo mockBookRepo;

    @Mock
    private ProductRepo mockProductRepo;
    private ServiceBook mockBookService;
    private AutoCloseable autoCloseable;

    @BeforeAll
    static void setUp(){
        bookRepo = new MemoryBookRepo();
        productRepo = new MemoryProductRepo();
        bookService = new ServiceBookImplementation(bookRepo, productRepo);

    }

    @BeforeEach
    void setUpMock(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockBookService = new ServiceBookImplementation(mockBookRepo, mockProductRepo);
    }


    @AfterEach
    void tearDown() throws Exception {
        bookRepo.deleteAll();
        productRepo.deleteAll();
        autoCloseable.close();
    }

    @Test
    @DisplayName("checks if returns the book from repo when found")
    void findBookById() {

        bookService.saveBook(dummyBook(1,"desc"));

        //checks if book is saved to repo
        assertThat(productRepo.existsById(1)).isEqualTo(true);
        assertThat(bookRepo.existsById(1)).isEqualTo(true);
        //checks if function can find a book that is saved to repo
        assertThat(bookService.findBookById(1).isPresent()).isEqualTo(true);
        assertThat(bookService.findBookById(1).get().getDescription()).isEqualTo("desc");
    }

    @Test
    @DisplayName("checks if returns all books")
    void findAllBooks() {

        int initial = bookService.getAllBooks().size();

        bookService.saveBook(dummyBook(initial+2, "desc1"));
        bookService.saveBook(dummyBook(initial+3, "desc2"));
        bookService.saveBook(dummyBook(initial+4, "desc3"));
        bookService.saveBook(dummyBook(initial+5, "desc4"));

        //checks if function finds any books

        assertThat(bookService.getAllBooks().isEmpty()).isEqualTo(false);
        //checks if function finds all the books saved in repository
        assertThat(bookService.getAllBooks().size()).isEqualTo(4 + initial);
        assertThat(productRepo.findAll().size()).isEqualTo(4+initial);
    }

    @Test
    @DisplayName("checks if returns empty optional when book is not in repo")
    void findBookByIdMissingID() {
        when(mockBookRepo.existsById(anyInt())).thenReturn(false);
        //checks if function returns empty optional when book is not found
        assertThat(mockBookService.findBookById(1).isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if returns empty optional when book is not in repo")
    void updateBookMissingID() {
        when(mockBookRepo.existsById(anyInt())).thenReturn(false);
        //checks if function returns empty optional when book is not found
        assertThat(mockBookService.updateBook(1, dummyBook(1,"old")).isPresent()).isEqualTo(false);
    }

 //   @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @DisplayName("checks if updates book correctly")
    void updateBook() {

        bookService.saveBook(dummyBook(1, "old"));
        assertThat(productRepo.existsById(1)).isEqualTo(true);
        assertThat(bookRepo.existsById(1)).isEqualTo(true);
  //      assertThat(bookRepo.findById(1)).isEqualTo(dummyBook(1,"old"));
        bookService.updateBook(1,dummyBook(1,"new"));

        assertThat(productRepo.findById(1).get().getDescription()).isEqualTo("new");
       assertThat(bookRepo.findById(1).get().getDescription()).isEqualTo("new");

    }

    @Test
    @DisplayName("checks if function changes the book correctly")
    void changeBook() {

        bookService.saveBook(
                new Book(2,56,"oldName", "author",
                        null, null, BookType.AUDIOBOOK,
                        45.6d, null));

        assertThat(productRepo.existsById(2)).isEqualTo(true);
        assertThat(bookRepo.existsById(2)).isEqualTo(true);

        bookService.changeBook(2, new Book(2,0,
                null, null,
                "new description",
                BookCategory.FANTASY, null, null, null));

        if(bookRepo.findById(2).isPresent()){
            var book = bookRepo.findById(2).get();
            assertThat(book.getIsbn()).isEqualTo(56);
            assertThat(book.getName()).isEqualTo("oldName");
            assertThat(book.getAuthor()).isEqualTo("author");
            assertThat(book.getDescription()).isEqualTo("new description");
            assertThat(book.getBookCategory()).isEqualTo(BookCategory.FANTASY);
            assertThat(book.getBookType()).isEqualTo(BookType.AUDIOBOOK);
            assertThat(book.getPrice()).isEqualTo(45.6d);
        }else throw new RuntimeException("couldn't find the book in the repo");

    }

    @Test
    @DisplayName("checks if returns empty optional when book is not in repo")
    void changeBookMissingID() {
        when(mockBookRepo.existsById(anyInt())).thenReturn(false);
        //checks if function returns empty optional when book is not found
        assertThat(mockBookService.changeBook(2, dummyBook(2,"old")).isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if returns empty optional when book is not in repo")
    void deleteBookByIdMissingID() {
        when(mockBookRepo.existsById(anyInt())).thenReturn(false);
        //checks if function returns empty optional when book is not found
        assertThat(mockBookService.deleteBookById(1).isPresent()).isEqualTo(false);
    }


    @Test
    @DisplayName("checks if deletes the book from repo correctly")
    void deleteBookById() {

        bookRepo.save(dummyBook(1,"desc"));
        bookService.deleteBookById(1);
        //checks if function returns empty optional when book is not found
        assertThat(bookRepo.existsById(1)).isEqualTo(false);
        assertThat(productRepo.existsById(1)).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if the books are properly filtered")
    void filterBooks(){

        var book1 = new Book(1,56,"book1", "sandie",
                null, BookCategory.FANTASY, BookType.AUDIOBOOK, 58.4, null);
        var book2 = new Book(2,36,"book2", "brandy",
                null, BookCategory.FANTASY, BookType.HARDCOVER, 38.4, null);
        var book3 = new Book(3,86,"book3", "sandie",
                null, BookCategory.CRIME, BookType.AUDIOBOOK, 78.4, null);
        var book4 = new Book(4,26,"book4", "LARRY",
                null, BookCategory.DRAMA, BookType.PAPERBACK, 98.4, null);
        var book5 = new Book(5,56,"book5", "sandie",
                null, BookCategory.FANTASY, BookType.AUDIOBOOK, 48.4, null);
        var book6 = new Book(6,83,"book6", "barry", null,null,
                null, 85.2,null);

        bookRepo.save(book1);
        bookRepo.save(book2);
        bookRepo.save(book3);
        bookRepo.save(book4);
        bookRepo.save(book5);
        bookRepo.save(book6);

        assertThat(bookService.getFilteredBooks("fantasy", "audiobook", 60d)).isEqualTo(Optional.of(List.of(book1, book5)));
        assertThat(bookService.getFilteredBooks("fantasy", "audiobook", 50d)).isEqualTo(Optional.of(List.of(book5)));
        assertThat(bookService.getFilteredBooks("fantasy", "hardcover", 50d)).isEqualTo(Optional.of(List.of(book2)));
        assertThat(bookService.getFilteredBooks("drama", "paperback", 100d)).isEqualTo(Optional.of(List.of(book4)));
        assertThat(bookService.getFilteredBooks("crime", "audiobook", 80d)).isEqualTo(Optional.of(List.of(book3)));
        assertThat(bookService.getFilteredBooks("crime", "audiobook", 30d)).isEqualTo(Optional.empty());
        assertThat(bookService.getFilteredBooks(null, "audiobook", 100d)).isEqualTo(Optional.of(List.of(book1, book3,book5)));
        assertThat(bookService.getFilteredBooks("fantasy", null, 100d)).isEqualTo(Optional.of(List.of(book1, book2,book5)));
        assertThat(bookService.getFilteredBooks(null, null, 100d)).isEqualTo(Optional.of(List.of(book1, book2, book3, book4, book5, book6)));
        assertThat(bookService.getFilteredBooks(null, null, 30d)).isEqualTo(Optional.empty());

    }

    private Book dummyBook(int id, String desc){
        return new Book(id, 47,"book", "author", desc, null, null, 56.3,null);
    }
}