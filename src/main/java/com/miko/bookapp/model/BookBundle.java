package com.miko.bookapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@AllArgsConstructor
@Data
@Table(name = "book_bundle")
@Entity
public class BookBundle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bundle_id")
    private long BundleID;

    private String description;

    @NotBlank(message = "discount cannot be blank")
    private Double discount;

    @Column(name = "combined_prize")
    private Double combinedPrize;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookBundle")
    private Set<Book> books;

    BookBundle(){
        double tempPrize = 0d;
        if(books!=null){
            for(Book book: books){
                if(book.getPrize()!=null)
                    tempPrize+=book.getPrize();
            }
        }

        combinedPrize = tempPrize;
    }

    public long getBundleID() {
        return BundleID;
    }

    public void setCombinedPrize(Double combinedPrize) {
        this.combinedPrize = combinedPrize;
    }

    void setBundleID(long bundleID) {
        BundleID = bundleID;
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
    }

    public Double getCombinedPrize() {
        return combinedPrize;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}