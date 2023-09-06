package com.miko.bookapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("UnusedReturnValue")
@AllArgsConstructor
@Table(name = "book_bundle")
@Entity
public class BookBundle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bundle_id")
    private long bundleID;

    private String description;

    @NotBlank(message = "discount cannot be blank")
    private Double discount;

    @Column(name = "combined_prize")
    private Double combinedPrize;

    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookBundle")
    private Set<Book> books;

    BookBundle(){
        combinedPrize = 0d;
        discount =0d;
    }
    public Set<Book> addBook(Book book){
        books.add(book);
        refreshCombinedPrize();
        return books;
    }

    public long getBundleID() {
        return bundleID;
    }

    private void setCombinedPrize(Double combinedPrize) {
        this.combinedPrize = combinedPrize;
    }

    void setBundleID(long bundleID) {
        this.bundleID = bundleID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
        combinedPrize = combinedPrize-(combinedPrize*discount);
    }
    public Double getCombinedPrize() {
        return combinedPrize;
    }

    public Set<Book> getBooks() {
        return books;
    }

//    public void setBooks(HashSet<Book> books) {
//        this.books = books;
//    }

    public Set<Book> deleteBook(long bookID){
        books.removeIf(book -> book.getId()==bookID);
        refreshCombinedPrize();
        return books;
    }
    private void refreshCombinedPrize(){
        double tempPrize = 0d;
        if(books!=null){
            for(Book book: books){
                if(book.getPrize()!=null)
                    tempPrize+=book.getPrize();
            }
        }

        this.setCombinedPrize(tempPrize*(1-discount));
    }

    @Override
    public String toString() {
        return "" + bundleID;

    }
}