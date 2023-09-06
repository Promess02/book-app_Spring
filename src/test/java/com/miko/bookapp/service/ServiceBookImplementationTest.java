package com.miko.bookapp.service;

import com.miko.bookapp.enums.BookCategory;
import com.miko.bookapp.enums.BookType;
import com.miko.bookapp.model.Book;
import com.miko.bookapp.repo.BookRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceBookImplementationTest {

    @Test
    @DisplayName("checks if returns the book from repo when found")
    void findBookById() {

        var mockBookRepo = inMemoryBookRepo();

        var mockBookService = new ServiceBookImplementation(mockBookRepo);

        mockBookService.saveBook(dummyBook(1,"desc"));

        //checks if book is saved to repo
        assertThat(mockBookRepo.existsById(1)).isEqualTo(true);
        //checks if function can find a book that is saved to repo
        assertThat(mockBookService.findBookById(1).isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("checks if returns empty optional when book is not in repo")
    void findBookByIdMissingID() {

        var mockBookRepo = mock(BookRepo.class);

        var mockBookService = new ServiceBookImplementation(mockBookRepo);

        when(mockBookRepo.existsById(anyInt())).thenReturn(false);
        //checks if function returns empty optional when book is not found
        assertThat(mockBookService.findBookById(1).isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if returns empty optional when book is not in repo")
    void updateBookMissingID() {
        var mockBookRepo = mock(BookRepo.class);

        var mockBookService = new ServiceBookImplementation(mockBookRepo);

        when(mockBookRepo.existsById(anyInt())).thenReturn(false);
        //checks if function returns empty optional when book is not found
        assertThat(mockBookService.updateBook(1, dummyBook(1,"old")).isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if updates book correctly")
    void updateBook() {
        var mockBookRepo = inMemoryBookRepo();

        var mockBookService = new ServiceBookImplementation(mockBookRepo);

        mockBookRepo.save(dummyBook(1,"old"));
        mockBookService.updateBook(1,dummyBook(1,"new"));

        if(mockBookRepo.findById(1).isPresent()) assertThat(mockBookRepo.findById(1).get().getDescription()).isEqualTo("new");

    }

    @Test
    @DisplayName("checks if function changes the book correctly")
    void changeBook() {
        var mockBookRepo = inMemoryBookRepo();
        var mockBookService = new ServiceBookImplementation(mockBookRepo);

        mockBookService.saveBook(
                new Book(2,56,"oldName", "author",
                        null, null, BookType.AUDIOBOOK,
                        45.6d, null));

        mockBookService.changeBook(2, new Book(2,0,
                null, null,
                "new description",
                BookCategory.FANTASY, null, null, null));

        if(mockBookRepo.findById(2).isPresent()){
            var book = mockBookRepo.findById(2).get();
            assertThat(book.getIsbn()).isEqualTo(56);
            assertThat(book.getName()).isEqualTo("oldName");
            assertThat(book.getAuthor()).isEqualTo("author");
            assertThat(book.getDescription()).isEqualTo("new description");
            assertThat(book.getBookCategory()).isEqualTo(BookCategory.FANTASY);
            assertThat(book.getBookType()).isEqualTo(BookType.AUDIOBOOK);
            assertThat(book.getPrize()).isEqualTo(45.6d);
        }else throw new RuntimeException("couldn't find the book in the repo");

    }

    @Test
    @DisplayName("checks if returns empty optional when book is not in repo")
    void changeBookMissingID() {
        var mockBookRepo = mock(BookRepo.class);

        var mockBookService = new ServiceBookImplementation(mockBookRepo);

        when(mockBookRepo.existsById(anyInt())).thenReturn(false);
        //checks if function returns empty optional when book is not found
        assertThat(mockBookService.changeBook(2, dummyBook(2,"old")).isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if returns empty optional when book is not in repo")
    void deleteBookByIdMissingID() {
        var mockBookRepo = mock(BookRepo.class);

        var mockBookService = new ServiceBookImplementation(mockBookRepo);

        when(mockBookRepo.existsById(anyInt())).thenReturn(false);
        //checks if function returns empty optional when book is not found
        assertThat(mockBookService.deleteBookById(1).isPresent()).isEqualTo(false);
    }


    @Test
    @DisplayName("checks if deletes the book from repo correctly")
    void deleteBookById() {
        var mockBookRepo = inMemoryBookRepo();

        var mockBookService = new ServiceBookImplementation(mockBookRepo);

        mockBookRepo.save(dummyBook(1,"desc"));
        mockBookService.deleteBookById(1);
        //checks if function returns empty optional when book is not found
        assertThat(mockBookRepo.existsById(1)).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if the books are properly filtered")
    void filterBooks(){
        var mockBookRepo = inMemoryBookRepo();
        var mockBookService = new ServiceBookImplementation(mockBookRepo);

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

        mockBookRepo.save(book1);
        mockBookRepo.save(book2);
        mockBookRepo.save(book3);
        mockBookRepo.save(book4);
        mockBookRepo.save(book5);

        assertThat(mockBookService.getFilteredBooks("fantasy", "audiobook", 60d)).isEqualTo(Optional.of(List.of(book1, book5)));
        assertThat(mockBookService.getFilteredBooks("fantasy", "audiobook", 50d)).isEqualTo(Optional.of(List.of(book5)));
        assertThat(mockBookService.getFilteredBooks("fantasy", "hardcover", 50d)).isEqualTo(Optional.of(List.of(book2)));
        assertThat(mockBookService.getFilteredBooks("drama", "paperback", 100d)).isEqualTo(Optional.of(List.of(book4)));
        assertThat(mockBookService.getFilteredBooks("crime", "audiobook", 80d)).isEqualTo(Optional.of(List.of(book3)));
        assertThat(mockBookService.getFilteredBooks("crime", "audiobook", 30d)).isEqualTo(Optional.empty());

    }

    private MemoryBookRepo inMemoryBookRepo(){
        return new MemoryBookRepo();
    }

    private Book dummyBook(int id, String desc){
        return new Book(id, 47,"dummyBook", "author", desc, null, null, 56.3,null);

    }
}