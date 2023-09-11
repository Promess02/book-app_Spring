package com.miko.bookapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.miko.bookapp.enums.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@SuppressWarnings("UnusedReturnValue")
@Entity
@Table(name = "bundle")
public class BookBundle extends Product {
    @NotBlank(message = "discount cannot be blank")
    private Double discount;
    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookBundle")
    private Set<Book> books;

    BookBundle(){
        discount =0d;
    }

    public BookBundle(long id, String description, Double discount, Double price, Set<Book> books) {
        super(id, description, price);
        this.discount = discount;
        this.books = books;
    }

    public Set<Book> addBook(Book book){
        books.add(book);
        refreshCombinedPrize();
        return books;
    }
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
        var price = this.getPrice();
        this.setPrice(price-(price*discount));
    }

    public Set<Book> getBooks() {
        return books;
    }


    public Set<Book> deleteBook(long bookID){
        books.removeIf(book -> book.getId()==bookID);
        refreshCombinedPrize();
        return books;
    }
    private void refreshCombinedPrize(){
        double tempPrize = 0d;
        if(books!=null){
            for(Book book: books){
                if(book.getPrice()!=null)
                    tempPrize+=book.getPrice();
            }
        }

        this.setPrice(tempPrize*(1-discount));
    }

    @Override
    public String toString() {
        return "" + getId();
    }
}